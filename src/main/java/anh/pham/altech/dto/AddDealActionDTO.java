package anh.pham.altech.dto;

import anh.pham.altech.constant.ActionType;
import anh.pham.altech.constant.UnitType;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddDealActionDTO {
    private ActionType actionType;
    private UUID targetProductId;
    //todo: ignore json
    private UUID dealId;
    private BigDecimal vl;
    private UnitType un;
}
