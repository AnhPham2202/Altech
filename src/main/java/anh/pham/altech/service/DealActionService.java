package anh.pham.altech.service;

import anh.pham.altech.dto.AddDealActionDTO;
import anh.pham.altech.dto.AddDealActionResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface DealActionService {
    AddDealActionResponseDTO add(AddDealActionDTO addDealActionDTO);
}
