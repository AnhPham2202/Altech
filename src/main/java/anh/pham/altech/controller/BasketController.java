package anh.pham.altech.controller;

import anh.pham.altech.constant.Constants;
import anh.pham.altech.dto.AddBasketDTO;
import anh.pham.altech.dto.AddBasketItemDTO;
import anh.pham.altech.dto.AddBasketItemResponseDTO;
import anh.pham.altech.dto.ReceiptResponseDTO;
import anh.pham.altech.service.BasketService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController()
public class BasketController {

    private final BasketService basketService;

    public BasketController(
        BasketService basketService
    ) {
        this.basketService = basketService;
    }

    @PostMapping(Constants.API_BASKET)
    public ResponseEntity<UUID> createNewBasket(@RequestBody AddBasketDTO body) {
        var result = this.basketService.add(body);
        return ResponseEntity.ok(result);
    }


    @PutMapping(Constants.API_BASKET)
    public ResponseEntity<AddBasketItemResponseDTO> updateBasket(@RequestBody AddBasketItemDTO body) throws BadRequestException {
        var result = this.basketService.update(body);
        return ResponseEntity.ok(result);
    }


    @GetMapping(Constants.API_BASKET)
    public ResponseEntity<ReceiptResponseDTO> calculate(@RequestParam UUID basketId) throws BadRequestException {
        var result = this.basketService.calculate(basketId);
        return ResponseEntity.ok(result);
    }
}
