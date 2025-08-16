package anh.pham.altech.service;

import anh.pham.altech.constant.ActionType;
import anh.pham.altech.constant.UnitType;
import anh.pham.altech.dto.AddBasketDTO;
import anh.pham.altech.dto.AddBasketItemDTO;
import anh.pham.altech.dto.AddBasketItemResponseDTO;
import anh.pham.altech.dto.ReceiptResponseDTO;
import anh.pham.altech.entity.Basket;
import anh.pham.altech.entity.BasketItem;
import anh.pham.altech.entity.Deal;
import anh.pham.altech.entity.DealAction;
import anh.pham.altech.entity.DealCondition;
import anh.pham.altech.entity.Product;
import anh.pham.altech.entity.User;
import anh.pham.altech.mapper.BasketItemMapper;
import anh.pham.altech.repository.BasketRepository;
import anh.pham.altech.service.impl.BasketServiceImpl;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UTBasketServiceImplTest {

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private ProductService productService;

    @Mock
    private DealService dealService;

    @Mock
    private UserService userService;

    @Mock
    private BasketItemMapper basketItemMapper;

    @InjectMocks
    private BasketServiceImpl basketService;

    private UUID basketId;
    private UUID productId;
    private User mockUser;
    private Product mockProduct;
    private Basket mockBasket;

    @BeforeEach
    void setUp() {
        basketId = UUID.randomUUID();
        productId = UUID.randomUUID();

        mockUser = new User();
        mockUser.setId(UUID.randomUUID());

        mockProduct = new Product();
        mockProduct.setId(productId);
        mockProduct.setPrice(BigDecimal.valueOf(100));
        mockProduct.setName("Test Product");

        mockBasket = new Basket();
        mockBasket.setId(basketId);
        mockBasket.setUser(mockUser);
        mockBasket.setItems(new HashMap<>());
    }

    @Test
    void testAdd_ShouldReturnBasketId() {
        AddBasketDTO dto = new AddBasketDTO();
        dto.setUserId(mockUser.getId());

        when(userService.getRefById(dto.getUserId())).thenReturn(mockUser);
        when(basketRepository.save(any(Basket.class))).thenAnswer(invocation -> {
            Basket saved = invocation.getArgument(0);
            saved.setId(basketId);
            return saved;
        });

        UUID result = basketService.add(dto);

        assertEquals(basketId, result);
        verify(basketRepository).save(any(Basket.class));
    }

    @Test
    void testUpdate_ShouldMergeItemAndReturnOk() throws BadRequestException {
        AddBasketItemDTO dto = new AddBasketItemDTO();
        dto.setBasketId(basketId);
        dto.setProductId(productId);
        dto.setQuantity(2);

        BasketItem newItem = new BasketItem();
        newItem.setProduct(mockProduct);
        newItem.setQuantity(2);

        when(basketRepository.findById(basketId)).thenReturn(Optional.of(mockBasket));
        when(basketItemMapper.fromAddDTOToEntity(dto)).thenReturn(newItem);

        AddBasketItemResponseDTO response = basketService.update(dto);

        assertTrue(response.isOk());
        assertEquals(1, mockBasket.getItems().size());
        verify(productService).decreaseStock(productId, 2);
        verify(basketRepository).save(mockBasket);
    }

    @Test
    void testCalculate_ShouldApplyDiscountsAndReturnReceipt() throws BadRequestException {
        BasketItem basketItem = new BasketItem();
        basketItem.setProduct(mockProduct);
        basketItem.setQuantity(2);
        mockBasket.getItems().put(mockProduct, basketItem);

        DealAction discountAction = new DealAction();
        discountAction.setTargetProduct(mockProduct);
        discountAction.setActionType(ActionType.DISCOUNT);
        discountAction.setUn(UnitType.PERCENT);
        discountAction.setVl(BigDecimal.valueOf(10));

        Deal mockDeal = new Deal();
        mockDeal.setId(UUID.randomUUID());
        mockDeal.setActions(List.of(discountAction));
        DealCondition condition = new DealCondition();
        condition.setProduct(mockProduct);
        mockDeal.setConditions(List.of(condition));

        when(basketRepository.findById(basketId)).thenReturn(Optional.of(mockBasket));
        when(dealService.getDealsForProduct(anyList())).thenReturn(List.of(mockDeal));

        ReceiptResponseDTO result = basketService.calculate(basketId);

        assertTrue(result.isOk());
        assertEquals(BigDecimal.valueOf(200), result.getProductPurchaseDTOList().get(0).getTotal());
        assertEquals(BigDecimal.valueOf(20), result.getDiscountPrice());
    }
}
