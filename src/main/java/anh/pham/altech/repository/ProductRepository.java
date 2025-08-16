package anh.pham.altech.repository;

import anh.pham.altech.entity.Product;
import anh.pham.altech.mapper.projection.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query(nativeQuery = true, value= "SELECT * FROM product where price <= :upperPrice and price >= :lowerPrice")
    Page<ProductDTO> findAllBy(
            @Param("upperPrice") BigDecimal upperPrice,
            @Param("lowerPrice") BigDecimal lowerPrice,
            Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM product where price <= :upperPrice and price >= :lowerPrice and stock = 0")
    Page<ProductDTO> findOutStockBy(
            @Param("upperPrice") BigDecimal upperPrice,
            @Param("lowerPrice") BigDecimal lowerPrice,
            Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM product where price <= :upperPrice and price >= :lowerPrice and stock > 0")
    Page<ProductDTO> findInStockBy(
            @Param("upperPrice") BigDecimal upperPrice,
            @Param("lowerPrice") BigDecimal lowerPrice,
            Pageable pageable);

    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock - :amount WHERE p.id = :id AND p.stock >= :amount")
    int decreaseStockIfEnough(@Param("id") UUID id, @Param("amount") int amount);

}
