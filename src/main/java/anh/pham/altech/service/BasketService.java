package anh.pham.altech.service;

import anh.pham.altech.dto.AddBasketDTO;
import anh.pham.altech.dto.AddBasketItemDTO;
import anh.pham.altech.dto.AddBasketItemResponseDTO;
import anh.pham.altech.dto.ReceiptResponseDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface BasketService {
    AddBasketItemResponseDTO update(AddBasketItemDTO basketItemDTO) throws BadRequestException;
    UUID add(AddBasketDTO basketDTO);
    ReceiptResponseDTO calculate(UUID basketId) throws BadRequestException;
}
