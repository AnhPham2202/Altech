package anh.pham.altech.service;

import anh.pham.altech.constant.Constants;
import anh.pham.altech.dto.*;
import anh.pham.altech.entity.Deal;
import anh.pham.altech.mapper.DealMapper;
import anh.pham.altech.repository.DealRepository;
import anh.pham.altech.service.impl.DealServiceImpl;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DealServiceImplTest {

    @Mock
    private DealRepository dealRepository;

    @Mock
    private DealConditionService dealConditionService;

    @Mock
    private DealActionService dealActionService;

    @Mock
    private DealMapper dealMapper;

    @InjectMocks
    private DealServiceImpl dealService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void add_shouldSaveDealAndAddConditionsAndActions() {
        AddDealDTO addDealDTO = new AddDealDTO();
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        addDealDTO.setProductIdList(Arrays.asList(productId1, productId2));

        AddDealConditionDTO condition1 = new AddDealConditionDTO();
        AddDealConditionDTO condition2 = new AddDealConditionDTO();
        addDealDTO.setDealConditionDTOList(Arrays.asList(condition1, condition2));

        AddDealActionDTO action1 = new AddDealActionDTO();
        AddDealActionDTO action2 = new AddDealActionDTO();
        addDealDTO.setAddDealActionDTOList(Arrays.asList(action1, action2));

        Deal newDeal = new Deal();
        when(dealMapper.fromAddDTOToEntity(addDealDTO)).thenReturn(newDeal);

        Deal savedDeal = new Deal();
        UUID savedDealId = UUID.randomUUID();
        savedDeal.setId(savedDealId);
        when(dealRepository.save(newDeal)).thenReturn(savedDeal);
        when(dealConditionService.add(any())).thenReturn(null);
        when(dealActionService.add(any())).thenReturn(null);


        AddDealResponseDTO response = dealService.add(addDealDTO);

        assertTrue(response.isOk());
        verify(dealMapper, times(1)).fromAddDTOToEntity(addDealDTO);
        verify(dealRepository, times(1)).save(newDeal);
        verify(dealConditionService, times(addDealDTO.getProductIdList().size() * addDealDTO.getDealConditionDTOList().size())).add(any());
        verify(dealActionService, times(addDealDTO.getProductIdList().size() * addDealDTO.getAddDealActionDTOList().size())).add(any());
    }

    @Test
    void add_shouldHandleEmptyLists() {
        AddDealDTO addDealDTO = new AddDealDTO();
        addDealDTO.setProductIdList(Collections.emptyList());
        addDealDTO.setDealConditionDTOList(Collections.emptyList());
        addDealDTO.setAddDealActionDTOList(Collections.emptyList());

        Deal newDeal = new Deal();
        when(dealMapper.fromAddDTOToEntity(addDealDTO)).thenReturn(newDeal);

        Deal savedDeal = new Deal();
        UUID savedDealId = UUID.randomUUID();
        savedDeal.setId(savedDealId);
        when(dealRepository.save(newDeal)).thenReturn(savedDeal);

        AddDealResponseDTO response = dealService.add(addDealDTO);

        assertTrue(response.isOk());
        verify(dealConditionService, never()).add(any());
        verify(dealActionService, never()).add(any());
    }

    @Test
    void edit_validDates_shouldCallUpdateDealDates() throws BadRequestException {
        UUID dealId = UUID.randomUUID();
        LocalDate startDate = LocalDate.of(2025, 8, 1);
        LocalDate endDate = LocalDate.of(2025, 8, 10);

        EditDealDTO dto = new EditDealDTO();
        dto.setId(dealId);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);

        doNothing().when(dealRepository).updateDealDates(dealId, startDate, endDate);

        EditDealResponseDTO response = dealService.edit(dto);

        verify(dealRepository, times(1)).updateDealDates(dealId, startDate, endDate);
        assertTrue(response.isOk());
    }

    @Test
    void edit_startDateAfterEndDate_shouldThrowBadRequestException() {
        EditDealDTO dto = new EditDealDTO();
        dto.setStartDate(LocalDate.of(2025, 8, 11));
        dto.setEndDate(LocalDate.of(2025, 8, 10));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            dealService.edit(dto);
        });

        assertEquals(Constants.BAD_REQUEST_DATE_TIME_BAD_FORMAT, ex.getMessage());
        verifyNoInteractions(dealRepository);
    }

    @Test
    void edit_updateThrowsException_shouldThrowException() throws BadRequestException {
        UUID dealId = UUID.randomUUID();
        LocalDate startDate = LocalDate.of(2025, 8, 1);
        LocalDate endDate = LocalDate.of(2025, 8, 10);

        EditDealDTO dto = new EditDealDTO();
        dto.setId(dealId);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);

        doThrow(new RuntimeException("DB error")).when(dealRepository).updateDealDates(dealId, startDate, endDate);

        assertThrows(RuntimeException.class, () -> {
            dealService.edit(dto);
        });

        verify(dealRepository, times(1)).updateDealDates(dealId, startDate, endDate);
    }

    @Test
    void getDealsForProduct_shouldReturnDeals() {
        List<UUID> productIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        List<Deal> dealList = Arrays.asList(new Deal(), new Deal());

        when(dealRepository.findDealsByProductId(productIds)).thenReturn(dealList);

        List<Deal> result = dealService.getDealsForProduct(productIds);

        assertEquals(dealList.size(), result.size());
        verify(dealRepository, times(1)).findDealsByProductId(productIds);
    }

    @Test
    void getDealsForProduct_emptyList_shouldReturnEmpty() {
        List<UUID> productIds = Collections.emptyList();

        when(dealRepository.findDealsByProductId(productIds)).thenReturn(Collections.emptyList());

        List<Deal> result = dealService.getDealsForProduct(productIds);

        assertTrue(result.isEmpty());
        verify(dealRepository, times(1)).findDealsByProductId(productIds);
    }
}
