package anh.pham.altech.mapper;

import anh.pham.altech.dto.AddProductDTO;
import anh.pham.altech.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends BaseAddMapper<Product, AddProductDTO> {
}
