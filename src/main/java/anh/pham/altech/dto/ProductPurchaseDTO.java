package anh.pham.altech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPurchaseDTO {
    private Integer quantity;
    private BigDecimal price;
    private List<DealAppliedDTO> dealAppliedDTOList;
    private BigDecimal total;
    private BigDecimal finalTotal;
}
