package anh.pham.altech.service.impl;

import anh.pham.altech.dto.AddDealActionDTO;
import anh.pham.altech.dto.AddDealActionResponseDTO;
import anh.pham.altech.entity.DealAction;
import anh.pham.altech.mapper.DealActionMapper;
import anh.pham.altech.repository.DealActionRepository;
import anh.pham.altech.service.DealActionService;
import org.springframework.stereotype.Service;

@Service
public class DealActionServiceImpl implements DealActionService {
    private final DealActionRepository dealActionRepository;
    private final DealActionMapper dealActionMapper;

    public DealActionServiceImpl(
        DealActionRepository dealActionRepository,
        DealActionMapper dealActionMapper
    ) {
        this.dealActionRepository = dealActionRepository;
        this.dealActionMapper = dealActionMapper;
    }


    @Override
    public AddDealActionResponseDTO add(AddDealActionDTO addDealActionDTO) {
        DealAction result = this.dealActionRepository.save(dealActionMapper.fromAddDTOToEntity(addDealActionDTO));
        return new AddDealActionResponseDTO();
    }
}
