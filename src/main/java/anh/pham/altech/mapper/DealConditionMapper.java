package anh.pham.altech.mapper;

import anh.pham.altech.dto.AddDealConditionDTO;
import anh.pham.altech.entity.Deal;
import anh.pham.altech.entity.DealCondition;
import anh.pham.altech.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface DealConditionMapper extends BaseAddMapper<DealCondition, AddDealConditionDTO> {
    @Override
    @Mapping(source = "dealId", target = "deal")
    @Mapping(source = "productId", target = "product")
    DealCondition fromAddDTOToEntity(AddDealConditionDTO dto);

    @Override
    @Mapping(source = "deal.id", target = "dealId")
    @Mapping(source = "product.id", target = "productId")
    AddDealConditionDTO fromEntityToAddDTO(DealCondition entity);

    default Deal mapDeal(UUID dealId) {
        if (dealId == null) return null;
        Deal deal = new Deal();
        deal.setId(dealId);
        return deal;
    }

    default Product mapProduct(UUID productId) {
        if (productId == null) return null;
        Product product = new Product();
        product.setId(productId);
        return product;
    }
}
