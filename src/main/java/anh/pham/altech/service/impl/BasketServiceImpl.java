package anh.pham.altech.service.impl;

import anh.pham.altech.constant.Constants;
import anh.pham.altech.constant.UnitType;
import anh.pham.altech.dto.AddBasketDTO;
import anh.pham.altech.dto.AddBasketItemDTO;
import anh.pham.altech.dto.AddBasketItemResponseDTO;
import anh.pham.altech.dto.DealAppliedDTO;
import anh.pham.altech.dto.ProductPurchaseDTO;
import anh.pham.altech.dto.ReceiptResponseDTO;
import anh.pham.altech.entity.Basket;
import anh.pham.altech.entity.BasketItem;
import anh.pham.altech.entity.Deal;
import anh.pham.altech.entity.DealAction;
import anh.pham.altech.entity.Product;
import anh.pham.altech.entity.User;
import anh.pham.altech.mapper.BasketItemMapper;
import anh.pham.altech.repository.BasketRepository;
import anh.pham.altech.service.BasketService;
import anh.pham.altech.service.DealService;
import anh.pham.altech.service.ProductService;
import anh.pham.altech.service.UserService;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BasketServiceImpl implements BasketService {
    private final BasketRepository basketRepository;
    private final ProductService productService;
    private final DealService dealService;
    private final UserService userService;
    private final BasketItemMapper basketItemMapper;

    public BasketServiceImpl(
        BasketItemMapper basketItemMapper,
        ProductService productService,
        UserService userService,
        DealService dealService,
        BasketRepository basketRepository
    ) {
        this.basketItemMapper = basketItemMapper;
        this.basketRepository = basketRepository;
        this.productService = productService;
        this.userService = userService;
        this.dealService = dealService;
    }

    @Override
    @Transactional
    public UUID add(AddBasketDTO addBasketDTO) {
        User user = userService.getRefById(addBasketDTO.getUserId());
        var basket = basketRepository.save(
            Basket
                .builder()
                .user(user)
                .build());

        return basket.getId();
    }

    @Override
    @Transactional
    public AddBasketItemResponseDTO update(AddBasketItemDTO addBasketDTO) throws BadRequestException {
        Basket basket = basketRepository.findById(addBasketDTO.getBasketId())
                .orElseThrow(() -> new BadRequestException(Constants.BAD_REQUEST_NOT_FOUND_ID));

        BasketItem item = basketItemMapper.fromAddDTOToEntity(addBasketDTO);

        productService.decreaseStock(addBasketDTO.getProductId(), addBasketDTO.getQuantity());

        basket.getItems().merge(item.getProduct(), item, (oldItem, newItem) -> {
            oldItem.setQuantity(oldItem.getQuantity() + newItem.getQuantity());
            return oldItem;
        });

        basketRepository.save(basket);

        return AddBasketItemResponseDTO.builder().ok(true).build();
    }

    @Override
    public ReceiptResponseDTO calculate(UUID basketId) throws BadRequestException {
        Basket basket = basketRepository.findById(basketId)
            .orElseThrow(() -> new BadRequestException(Constants.BAD_REQUEST_NOT_FOUND_ID));

        Map<Product, BasketItem> items = basket.getItems();

        List<UUID> productIds = items.keySet().stream()
            .map(Product::getId)
            .toList();

        List<Deal> deals = dealService.getDealsForProduct(productIds);

        List<ProductPurchaseDTO> productPurchaseDTOList = new ArrayList<>();

        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (Map.Entry<Product, BasketItem> entry : items.entrySet()) {
            Product product = entry.getKey();
            BasketItem basketItem = entry.getValue();

            BigDecimal price = product.getPrice();
            int quantity = basketItem.getQuantity();
            BigDecimal total = price.multiply(BigDecimal.valueOf(quantity));

            List<DealAppliedDTO> appliedDealsForProduct = new ArrayList<>();

            BigDecimal discountAmountForProduct = BigDecimal.ZERO;
            int additionalQuantity = 0;

            List<Deal> applicableDeals = filterApplicableDeals(deals, items, product);

            for (Deal deal : applicableDeals) {
                for (DealAction action : deal.getActions()) {
                    if (action.getTargetProduct() == null || !action.getTargetProduct().equals(product)) {
                        continue;
                    }

                    BigDecimal actionValue = action.getVl();
                    switch (action.getActionType()) {
                        case DISCOUNT -> {
                            BigDecimal discountThis;
                            if (action.getUn() == UnitType.PERCENT) {
                                discountThis = total.multiply(actionValue).divide(BigDecimal.valueOf(100));
                            } else if (action.getUn() == UnitType.AMOUNT) {
                                discountThis = actionValue;
                            } else {
                                discountThis = BigDecimal.ZERO;
                            }
                            discountAmountForProduct = discountAmountForProduct.add(discountThis);

                            appliedDealsForProduct.add(
                                DealAppliedDTO.builder()
                                    .dealId(deal.getId())
                                    .actionType(action.getActionType())
                                    .build()
                            );
                        }
                        case ADDITIONAL -> {
                            if (action.getUn() == UnitType.QUANTITY) {
                                additionalQuantity += actionValue.intValue();
                            }
                            appliedDealsForProduct.add(
                                DealAppliedDTO.builder()
                                    .dealId(deal.getId())
                                    .actionType(action.getActionType())
                                    .build()
                            );
                        }
                        default -> {
                        }
                    }
                }
            }

            BigDecimal finalTotal = total.subtract(discountAmountForProduct).max(BigDecimal.ZERO);

            ProductPurchaseDTO productPurchaseDTO = ProductPurchaseDTO.builder()
                .quantity(quantity + additionalQuantity)
                .price(price)
                .total(total)
                .finalTotal(finalTotal)
                .dealAppliedDTOList(appliedDealsForProduct)
                .build();

            productPurchaseDTOList.add(productPurchaseDTO);

            totalDiscount = totalDiscount.add(discountAmountForProduct);
        }

        BigDecimal totalPrice = productPurchaseDTOList.stream()
            .map(ProductPurchaseDTO::getFinalTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        ReceiptResponseDTO response = new ReceiptResponseDTO();
        response.setOk(true);
        response.setTotal(totalPrice);
        response.setDiscountPrice(totalDiscount);
        response.setProductPurchaseDTOList(productPurchaseDTOList);

        return response;
    }

    private List<Deal> filterApplicableDeals(List<Deal> deals, Map<Product, BasketItem> items, Product product) {
        return deals.stream()
            .filter(deal -> deal.getConditions().stream()
                    .anyMatch(cond -> cond.getProduct().equals(product))
            )
            .filter(deal -> deal.getConditions().stream().allMatch(condition -> {
                BasketItem bi = items.get(condition.getProduct());
                if (bi == null) return false;
                if (condition.getMinQuantity() != null && bi.getQuantity() < condition.getMinQuantity()) return false;
                if (condition.getMinAmount() != null) {
                    BigDecimal biTotal = bi.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(bi.getQuantity()));
                    if (biTotal.compareTo(condition.getMinAmount()) < 0) return false;
                }
                return true;
            }))
            .toList();
    }
}
