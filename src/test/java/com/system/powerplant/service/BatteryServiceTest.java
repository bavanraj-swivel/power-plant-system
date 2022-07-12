package com.system.powerplant.service;

import com.system.powerplant.domain.request.BatteryRequestDto;
import com.system.powerplant.exception.PowerPlantApplicationException;
import com.system.powerplant.repository.BatteryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * This class tests the {@link BatteryService } class.
 */
class BatteryServiceTest {

    private static final int START_VALUE = 0;
    private static final int END_VALUE = 10500;
    private final List<BatteryRequestDto> batteryRequestDtoList = getBatteryRequestDto();
    private BatteryService batteryService;
    @Mock
    private BatteryRepository batteryRepository;

    @BeforeEach
    void setUp() {
        openMocks(this);
        batteryService = new BatteryService(batteryRepository);
    }

    /**
     * Unit tests for add bulk battery details.
     */

    @Test
    void Should_SaveBatteryDetailOnDatabase_When_ValidDataIsGiven() {
        batteryService.addBulkBatteries(batteryRequestDtoList);
        verify(batteryRepository, times(1)).saveAll(anyList());
    }

    @Test
    void Should_ThrowPowerPlantApplicationException_When_DataAccessExceptionIsThrownWhileSavingData() {
        when(batteryRepository.saveAll(anyList())).thenThrow(new DataAccessException("Failed") {
        });
        PowerPlantApplicationException exception = assertThrows(PowerPlantApplicationException.class, () -> {
            batteryService.addBulkBatteries(batteryRequestDtoList);
        });
        assertEquals("Failed to save battery detail list to database.", exception.getMessage());
    }

    /**
     * Unit tests for get batteries in postcode range.
     */

    @Test
    void Should_ReturnBatteryList_When_ValidRangeIsGiven() {
        batteryService.getBatteriesInPostalCodeRange(START_VALUE, END_VALUE);
        verify(batteryRepository, times(1))
                .findByPostcodeIsBetween(eq(START_VALUE), eq(END_VALUE), any(Sort.class));
    }

    @Test
    void Should_ThrowPowerPlantApplicationException_When_DataAccessExceptionIsThrownWhileGettingData() {
        when(batteryRepository.findByPostcodeIsBetween(eq(START_VALUE), eq(END_VALUE), any(Sort.class)))
                .thenThrow(new DataAccessException("Failed") {
                });
        PowerPlantApplicationException exception = assertThrows(PowerPlantApplicationException.class, () -> {
            batteryService.getBatteriesInPostalCodeRange(START_VALUE, END_VALUE);
        });
        assertEquals("Failed to get batteries in postal code range.", exception.getMessage());
    }

    private List<BatteryRequestDto> getBatteryRequestDto() {
        List<BatteryRequestDto> batteryList = new ArrayList<>();
        BatteryRequestDto batteryRequestDto = new BatteryRequestDto();
        ReflectionTestUtils.setField(batteryRequestDto, "name", "CEB");
        ReflectionTestUtils.setField(batteryRequestDto, "postcode", 10400);
        ReflectionTestUtils.setField(batteryRequestDto, "watts", 150.0);
        batteryList.add(batteryRequestDto);
        return batteryList;
    }
}