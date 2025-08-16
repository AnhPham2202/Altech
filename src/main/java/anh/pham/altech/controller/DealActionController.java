package anh.pham.altech.controller;

import anh.pham.altech.constant.Constants;
import anh.pham.altech.dto.AddDealActionDTO;
import anh.pham.altech.dto.AddDealActionResponseDTO;
import anh.pham.altech.service.DealActionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@Deprecated
public class DealActionController {
    private final DealActionService dealActionService;

    public DealActionController (DealActionService dealActionService) {
        this.dealActionService = dealActionService;
    }

    @PostMapping(Constants.API_DEAL_ACTION)
    public ResponseEntity<AddDealActionResponseDTO> addDealAction(@RequestBody AddDealActionDTO body) {
        var result = this.dealActionService.add(body);
        return ResponseEntity.ok(result);
    }
}
