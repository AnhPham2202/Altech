package anh.pham.altech.repository;

import anh.pham.altech.entity.Deal;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DealRepository extends JpaRepository<Deal, UUID> {
    @Query("SELECT d FROM Deal d JOIN d.conditions c WHERE c.product.id IN :productIds")
    List<Deal> findDealsByProductId(@Param("productIds") List<UUID> productIds);


    @Modifying
    @Query("""
        UPDATE Deal d
        SET d.startDate = :startDate,
            d.endDate = :endDate
        WHERE d.id = :id
    """)
    void updateDealDates(@Param("id") UUID id,
                         @Param("startDate") LocalDate startDate,
                         @Param("endDate") LocalDate endDate);
}
