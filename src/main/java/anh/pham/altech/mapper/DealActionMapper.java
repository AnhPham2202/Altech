package anh.pham.altech.mapper;

import anh.pham.altech.dto.AddDealActionDTO;
import anh.pham.altech.entity.Deal;
import anh.pham.altech.entity.DealAction;
import anh.pham.altech.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface DealActionMapper extends BaseAddMapper<DealAction, AddDealActionDTO> {
    @Override
    @Mapping(source = "targetProductId", target = "targetProduct")
    @Mapping(source = "dealId", target = "deal")
    DealAction fromAddDTOToEntity(AddDealActionDTO dto);

    @Override
    @Mapping(source = "targetProduct.id", target = "targetProductId")
    @Mapping(source = "deal.id", target = "dealId")
    AddDealActionDTO fromEntityToAddDTO(DealAction entity);

    default Product mapTargetProduct(UUID targetProductId) {
        if (targetProductId == null) return null;
        Product targetProduct = new Product();
        targetProduct.setId(targetProductId);
        return targetProduct;
    }


    default Deal mapDeal(UUID dealId) {
        if (dealId == null) return null;
        Deal deal = new Deal();
        deal.setId(dealId);
        return deal;
    }
}
