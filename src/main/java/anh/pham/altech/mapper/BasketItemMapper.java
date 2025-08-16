package anh.pham.altech.mapper;

import anh.pham.altech.dto.AddBasketItemDTO;
import anh.pham.altech.entity.Basket;
import anh.pham.altech.entity.BasketItem;
import anh.pham.altech.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface BasketItemMapper extends BaseAddMapper<BasketItem, AddBasketItemDTO> {
    @Override
    @Mapping(source = "basketId", target = "basket")
    @Mapping(source = "productId", target = "product")
    BasketItem fromAddDTOToEntity(AddBasketItemDTO dto);

    @Override
    @Mapping(source = "basket.id", target = "basketId")
    @Mapping(source = "product.id", target = "productId")
    AddBasketItemDTO fromEntityToAddDTO(BasketItem entity);

    default Basket mapBasket(UUID basketId) {
        if (basketId == null) return null;
        Basket basket = new Basket();
        basket.setId(basketId);
        return basket;
    }

    default Product mapProduct(UUID productId) {
        if (productId == null) return null;
        Product product = new Product();
        product.setId(productId);
        return product;
    }
}
