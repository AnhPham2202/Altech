package anh.pham.altech.service.impl;

import anh.pham.altech.dto.AddDealConditionDTO;
import anh.pham.altech.dto.AddDealConditionResponseDTO;
import anh.pham.altech.dto.GetDealResponseDTO;
import anh.pham.altech.entity.DealCondition;
import anh.pham.altech.mapper.DealConditionMapper;
import anh.pham.altech.repository.DealConditionRepository;
import anh.pham.altech.service.DealConditionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DealConditionServiceImpl implements DealConditionService {
    private final DealConditionRepository dealConditionRepository;
    private final DealConditionMapper dealConditionMapper;

    public DealConditionServiceImpl(
        DealConditionRepository dealConditionRepository,
        DealConditionMapper dealConditionMapper
    ) {
        this.dealConditionRepository = dealConditionRepository;
        this.dealConditionMapper = dealConditionMapper;
    }


    @Override
    public AddDealConditionResponseDTO add(AddDealConditionDTO addDealConditionDTO) {
        DealCondition result = this.dealConditionRepository.save(dealConditionMapper.fromAddDTOToEntity(addDealConditionDTO));
        return new AddDealConditionResponseDTO();
    }

}
