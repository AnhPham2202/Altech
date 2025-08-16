package anh.pham.altech.service;

import anh.pham.altech.constant.StockStatus;
import anh.pham.altech.dto.AddProductDTO;
import anh.pham.altech.dto.AddProductResponseDTO;
import anh.pham.altech.dto.DeleteProductDTO;
import anh.pham.altech.dto.DeleteProductResponseDTO;
import anh.pham.altech.mapper.projection.ProductDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public interface ProductService {
    Page<ProductDTO> getAll(BigDecimal upperPrice, BigDecimal lowerPrice, StockStatus stockStatus, Pageable pageable);
    AddProductResponseDTO add(AddProductDTO productDTO);
    void decreaseStock(UUID productId, Integer amount) throws BadRequestException;
    DeleteProductResponseDTO delete(DeleteProductDTO body);
}
