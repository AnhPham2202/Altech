package anh.pham.altech.mapper;

import anh.pham.altech.dto.AddDealDTO;
import anh.pham.altech.entity.Deal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DealMapper extends BaseAddMapper<Deal, AddDealDTO> {
}
