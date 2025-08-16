package anh.pham.altech.service;

import anh.pham.altech.dto.AddDealConditionDTO;
import anh.pham.altech.dto.AddDealConditionResponseDTO;
import anh.pham.altech.dto.GetDealResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DealConditionService {
    AddDealConditionResponseDTO add(AddDealConditionDTO addDealConditionDTO);
}
