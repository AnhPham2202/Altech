package anh.pham.altech.repository;

import anh.pham.altech.entity.DealCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DealConditionRepository extends JpaRepository<DealCondition, UUID> {

}
