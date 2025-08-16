package anh.pham.altech.controller;

import anh.pham.altech.constant.Constants;
import anh.pham.altech.constant.StockStatus;
import anh.pham.altech.dto.AddProductDTO;
import anh.pham.altech.dto.AddProductResponseDTO;
import anh.pham.altech.dto.DeleteProductDTO;
import anh.pham.altech.dto.DeleteProductResponseDTO;
import anh.pham.altech.mapper.projection.ProductDTO;
import anh.pham.altech.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController()
public class ProductController {
    private final ProductService productService;

    public ProductController (
        ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(Constants.API_PRODUCT)
    public ResponseEntity<Page<ProductDTO>> getProducts(
            @RequestParam BigDecimal upperPrice,
            @RequestParam BigDecimal lowerPrice,
            @RequestParam StockStatus stockStatus,
            Pageable pageable) {
        return ResponseEntity.ok(productService.getAll(upperPrice, lowerPrice, stockStatus, pageable));
    }

    @PostMapping(Constants.API_PRODUCT)
    public ResponseEntity<AddProductResponseDTO> getProducts(@RequestBody AddProductDTO productDTO) {
        return ResponseEntity.ok(productService.add(productDTO));
    }

    @DeleteMapping(Constants.API_PRODUCT)
    public ResponseEntity<DeleteProductResponseDTO> deleteProducts(@RequestBody DeleteProductDTO body) {
        return ResponseEntity.ok(productService.delete(body));
    }
}
