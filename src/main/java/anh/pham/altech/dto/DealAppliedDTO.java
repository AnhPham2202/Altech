package anh.pham.altech.dto;

import anh.pham.altech.constant.ActionType;
import anh.pham.altech.constant.DealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DealAppliedDTO {
    private UUID dealId;
    private DealType dealType;
    private ActionType actionType;
}
