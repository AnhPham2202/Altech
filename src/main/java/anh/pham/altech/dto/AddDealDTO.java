package anh.pham.altech.dto;

import anh.pham.altech.constant.DealType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AddDealDTO {
    private DealType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<AddDealConditionDTO> dealConditionDTOList;
    private List<AddDealActionDTO> addDealActionDTOList;
    private List<UUID> productIdList;
}
