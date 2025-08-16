package anh.pham.altech.service.impl;

import anh.pham.altech.constant.Constants;
import anh.pham.altech.dto.*;
import anh.pham.altech.entity.Deal;
import anh.pham.altech.mapper.DealMapper;
import anh.pham.altech.repository.DealRepository;
import anh.pham.altech.service.DealActionService;
import anh.pham.altech.service.DealConditionService;
import anh.pham.altech.service.DealService;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DealServiceImpl implements DealService {
    private final DealRepository dealRepository;
    private final DealConditionService dealConditionService;
    private final DealActionService dealActionService;
    private final DealMapper dealMapper;

    public DealServiceImpl(
        DealRepository dealRepository,
        DealActionService dealActionService,
        DealConditionService dealConditionService,
        DealMapper dealMapper
    ) {
        this.dealConditionService = dealConditionService;
        this.dealRepository = dealRepository;
        this.dealMapper = dealMapper;
        this.dealActionService = dealActionService;
    }

    @Override
    @Transactional
    public AddDealResponseDTO add(AddDealDTO addDealDTO) {
        Deal deal = dealMapper.fromAddDTOToEntity(addDealDTO);

        Deal savedDeal = dealRepository.save(deal);
        List<UUID> productIds = addDealDTO.getProductIdList();

        for (UUID productId : productIds) {
            for (AddDealConditionDTO dealConditionDTO : addDealDTO.getDealConditionDTOList()) {
                dealConditionDTO.setDealId(savedDeal.getId());
                dealConditionDTO.setProductId(productId);
                dealConditionService.add(dealConditionDTO);
            }

            for (AddDealActionDTO dealActionDTO : addDealDTO.getAddDealActionDTOList()) {
                dealActionDTO.setDealId(savedDeal.getId());
                dealActionService.add(dealActionDTO);
            }
        }

        return AddDealResponseDTO.builder()
            .ok(true)
            .build();
    }

    @Override
    @Transactional
    public EditDealResponseDTO edit(EditDealDTO editDealDTO) throws BadRequestException {
        if (editDealDTO.getStartDate().isAfter(editDealDTO.getEndDate())) {
            throw new BadRequestException(Constants.BAD_REQUEST_DATE_TIME_BAD_FORMAT);
        }
        dealRepository.updateDealDates(editDealDTO.getId(), editDealDTO.getStartDate(), editDealDTO.getEndDate());
        return EditDealResponseDTO.builder()
            .ok(true)
            .build();
    }

    @Override
    public List<Deal> getDealsForProduct(List<UUID> productIds) {
        return dealRepository.findDealsByProductId(productIds);
    }
}
