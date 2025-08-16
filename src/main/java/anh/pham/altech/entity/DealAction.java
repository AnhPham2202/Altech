package anh.pham.altech.entity;


import anh.pham.altech.constant.ActionType;
import anh.pham.altech.constant.UnitType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "deal_action")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealAction {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "deal_id", nullable = false)
    private Deal deal;

    @ManyToOne
    @JoinColumn(name = "target_product_id")
    private Product targetProduct;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    private BigDecimal vl;

    @Enumerated(EnumType.STRING)
    private UnitType un;

    private String description;

}
