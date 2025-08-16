package anh.pham.altech.service;

import anh.pham.altech.dto.AddDealDTO;
import anh.pham.altech.dto.AddDealResponseDTO;
import anh.pham.altech.dto.EditDealDTO;
import anh.pham.altech.dto.EditDealResponseDTO;
import anh.pham.altech.entity.Deal;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DealService {
    AddDealResponseDTO add(AddDealDTO addDealDTO);

    EditDealResponseDTO edit(EditDealDTO editDealDTO) throws BadRequestException;
    List<Deal> getDealsForProduct(List<UUID> productIds);
}
