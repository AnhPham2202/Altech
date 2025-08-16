package anh.pham.altech.service;


import anh.pham.altech.constant.Constants;
import anh.pham.altech.constant.StockStatus;
import anh.pham.altech.dto.AddProductDTO;
import anh.pham.altech.dto.AddProductResponseDTO;
import anh.pham.altech.dto.DeleteProductDTO;
import anh.pham.altech.entity.Product;
import anh.pham.altech.mapper.ProductMapper;
import anh.pham.altech.mapper.projection.ProductDTO;
import anh.pham.altech.repository.ProductRepository;
import anh.pham.altech.service.impl.ProductServiceImpl;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_ShouldCallFindInStockBy_WhenStockStatusInStock() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> expectedPage = new PageImpl<>(List.of());

        when(productRepository.findInStockBy(any(), any(), eq(pageable)))
            .thenReturn(expectedPage);

        Page<ProductDTO> result = productService.getAll(BigDecimal.TEN, BigDecimal.ONE, StockStatus.IN_STOCK, pageable);

        assertThat(result).isSameAs(expectedPage);
        verify(productRepository).findInStockBy(BigDecimal.TEN, BigDecimal.ONE, pageable);
    }

    @Test
    void getAll_ShouldCallFindOutStockBy_WhenStockStatusOutOfStock() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> expectedPage = new PageImpl<>(List.of());

        when(productRepository.findOutStockBy(any(), any(), eq(pageable)))
            .thenReturn(expectedPage);

        Page<ProductDTO> result = productService.getAll(BigDecimal.TEN, BigDecimal.ONE, StockStatus.OUT_OF_STOCK, pageable);

        assertThat(result).isSameAs(expectedPage);
        verify(productRepository).findOutStockBy(BigDecimal.TEN, BigDecimal.ONE, pageable);
    }

    @Test
    void getAll_ShouldCallFindAllBy_WhenStockStatusNull() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> expectedPage = new PageImpl<>(List.of());

        when(productRepository.findAllBy(any(), any(), eq(pageable)))
            .thenReturn(expectedPage);

        Page<ProductDTO> result = productService.getAll(BigDecimal.TEN, BigDecimal.ONE, null, pageable);

        assertThat(result).isSameAs(expectedPage);
        verify(productRepository).findAllBy(BigDecimal.TEN, BigDecimal.ONE, pageable);
    }

    @Test
    void add_ShouldSaveProduct_AndReturnResponse() {
        AddProductDTO dto = new AddProductDTO("Test", BigDecimal.TEN, 5);
        Product productEntity = new Product();
        productEntity.setId(UUID.randomUUID());

        when(productMapper.fromAddDTOToEntity(dto)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(productEntity);

        AddProductResponseDTO response = productService.add(dto);

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();
        assertThat(response.getId()).isEqualTo(productEntity.getId());
        verify(productRepository).save(productEntity);
    }

    @Test
    void decreaseStock_ShouldSucceed_WhenUpdatedRowsGreaterThanZero() throws Exception {
        UUID id = UUID.randomUUID();
        when(productRepository.decreaseStockIfEnough(id, 5)).thenReturn(1);

        productService.decreaseStock(id, 5);

        verify(productRepository).decreaseStockIfEnough(id, 5);
    }

    @Test
    void decreaseStock_ShouldThrow_WhenUpdatedRowsZero() {
        UUID id = UUID.randomUUID();
        when(productRepository.decreaseStockIfEnough(id, 5)).thenReturn(0);

        assertThatThrownBy(() -> productService.decreaseStock(id, 5))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(Constants.BAD_REQUEST_INSUFFICIENT_AMOUNT);

        verify(productRepository).decreaseStockIfEnough(id, 5);
    }

    @Test
    void delete_ShouldCallRepositoryDeleteAllById() {
        DeleteProductDTO dto = new DeleteProductDTO();
        List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID());
        dto.setIds(ids);

        productService.delete(dto);

        verify(productRepository).deleteAllById(ids);
    }
}
