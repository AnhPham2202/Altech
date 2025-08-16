package anh.pham.altech.controller;

import anh.pham.altech.constant.Constants;
import anh.pham.altech.dto.AddDealDTO;
import anh.pham.altech.dto.AddDealResponseDTO;
import anh.pham.altech.dto.EditDealDTO;
import anh.pham.altech.dto.EditDealResponseDTO;
import anh.pham.altech.service.DealService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class DealController {
    private final DealService dealService;

    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @PostMapping(Constants.API_DEAL)
    public ResponseEntity<AddDealResponseDTO> addDeal(@RequestBody AddDealDTO body) {
        return ResponseEntity.ok(this.dealService.add(body));
    }

    @PutMapping(Constants.API_DEAL)
    public ResponseEntity<EditDealResponseDTO> editDeal(@RequestBody EditDealDTO editDealDTO) throws BadRequestException {
        return ResponseEntity.ok(dealService.edit(editDealDTO));
    }

}
