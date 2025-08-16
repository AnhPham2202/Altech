package anh.pham.altech.controller;

import anh.pham.altech.constant.Constants;
import anh.pham.altech.dto.AddDealConditionDTO;
import anh.pham.altech.dto.AddDealConditionResponseDTO;
import anh.pham.altech.service.DealConditionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@Deprecated
public class DealConditionController {
    private final DealConditionService dealConditionService;

    public DealConditionController(DealConditionService dealConditionService) {
        this.dealConditionService = dealConditionService;
    }

    @PostMapping(Constants.API_DEAL_CONDITION)
    public ResponseEntity<AddDealConditionResponseDTO> addDealCondition(@RequestBody AddDealConditionDTO body) {
        var result = this.dealConditionService.add(body);
        return ResponseEntity.ok(result);
    }

}
