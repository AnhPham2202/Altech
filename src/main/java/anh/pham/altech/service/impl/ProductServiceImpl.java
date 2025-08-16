package anh.pham.altech.service.impl;

import anh.pham.altech.constant.Constants;
import anh.pham.altech.constant.StockStatus;
import anh.pham.altech.dto.AddProductDTO;
import anh.pham.altech.dto.AddProductResponseDTO;
import anh.pham.altech.dto.DeleteProductDTO;
import anh.pham.altech.dto.DeleteProductResponseDTO;
import anh.pham.altech.entity.Product;
import anh.pham.altech.mapper.ProductMapper;
import anh.pham.altech.mapper.projection.ProductDTO;
import anh.pham.altech.repository.ProductRepository;
import anh.pham.altech.service.ProductService;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(
        ProductRepository productRepository,
        ProductMapper productMapper
    ) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    @Override
    public Page<ProductDTO> getAll(BigDecimal upperPrice, BigDecimal lowerPrice, StockStatus stockStatus, Pageable pageable) {
        stockStatus = stockStatus == null ? StockStatus.ALL : stockStatus;
        return switch (stockStatus) {
            case IN_STOCK ->   productRepository.findInStockBy(upperPrice, lowerPrice, pageable);
            case OUT_OF_STOCK -> productRepository.findOutStockBy(upperPrice, lowerPrice, pageable);
            default ->  productRepository.findAllBy(upperPrice, lowerPrice, pageable);
        };
    }

    @Override
    @Transactional
    public AddProductResponseDTO add(AddProductDTO productDTO) {
        Product product = productRepository.save(
            productMapper.fromAddDTOToEntity(productDTO)
        );
        return AddProductResponseDTO.builder()
            .ok(true)
            .id(product.getId())
            .build();
    }


    @Override
    @Transactional
    public void decreaseStock(UUID productId, Integer amount) throws BadRequestException {
        int updatedRows = productRepository.decreaseStockIfEnough(productId, amount);

        if (updatedRows == 0) {
            throw new BadRequestException(Constants.BAD_REQUEST_INSUFFICIENT_AMOUNT);
        }
    }

    @Override
    @Transactional
    public DeleteProductResponseDTO delete(DeleteProductDTO body) {
        this.productRepository.deleteAllById(body.getIds());
        return DeleteProductResponseDTO.builder().ok(true).build();
    }
}
