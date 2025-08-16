package anh.pham.altech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddDealConditionDTO {
    private Integer minQuantity;
    private BigDecimal minAmount;
    private UUID productId;
    //todo: ignore json
    private UUID dealId;
}
