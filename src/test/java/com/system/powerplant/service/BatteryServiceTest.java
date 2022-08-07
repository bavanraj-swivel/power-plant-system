package com.system.powerplant.service;

import com.system.powerplant.domain.entity.Battery;
import com.system.powerplant.domain.request.BatteryRequestDto;
import com.system.powerplant.domain.response.BatteryListResponseDto;
import com.system.powerplant.exception.PowerPlantApplicationException;
import com.system.powerplant.exception.RequiredFieldsMissingException;
import com.system.powerplant.repository.BatteryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;

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
        batteryService.addBulkBatteries(List.of(new BatteryRequestDto("CEB", 10400, 500.0)));
        verify(batteryRepository, times(1)).saveAll(anyList());
    }

    @Test
    void Should_ThrowRequiredFieldsMissingException_When_RequiredFieldIsMissing() {
        BatteryRequestDto batteryRequestDto = new BatteryRequestDto("", 10400, 500.0);
        List<BatteryRequestDto> batteryList = List.of(batteryRequestDto);
        RequiredFieldsMissingException exception = assertThrows(RequiredFieldsMissingException.class, () -> {
            batteryService.addBulkBatteries(batteryList);
        });
        assertEquals("Required fields are missing for battery request: " + batteryRequestDto.toLogJson(),
                exception.getMessage());
    }

    @Test
    void Should_ThrowPowerPlantApplicationException_When_DataAccessExceptionIsThrownWhileSavingData() {
        List<BatteryRequestDto> batteryList = List.of(new BatteryRequestDto("CEB", 10400, 500.0));
        when(batteryRepository.saveAll(anyList())).thenThrow(new DataAccessException("Failed") {
        });
        PowerPlantApplicationException exception = assertThrows(PowerPlantApplicationException.class, () -> {
            batteryService.addBulkBatteries(batteryList);
        });
        assertEquals("Failed to save battery detail list to database.", exception.getMessage());
    }

    /**
     * Unit tests for get batteries in postcode range.
     */

    @Test
    void Should_ReturnBatteryNameListInAlphabeticOrder_When_ValidRangeIsGiven() {
        Battery battery1 = new Battery();
        battery1.setName("LECO");
        battery1.setPostcode(10400);
        battery1.setWatts(100.0);

        Battery battery2 = new Battery();
        battery2.setName("CEB");
        battery2.setPostcode(10500);
        battery2.setWatts(500.0);

        when(batteryRepository.findByPostcodeIsBetween(START_VALUE, END_VALUE)).thenReturn(List.of(battery1, battery2));

        BatteryListResponseDto batteryList = batteryService.getBatteriesInPostalCodeRange(START_VALUE, END_VALUE);
        assertEquals("CEB", batteryList.getBatteries().get(0));
        assertEquals("LECO", batteryList.getBatteries().get(1));
        assertEquals(2, batteryList.getBatterySummary().getTotalBatteries());
        assertEquals(600.0, batteryList.getBatterySummary().getTotalWatts());
        assertEquals(300.0, batteryList.getBatterySummary().getAverageWatts());
    }

    @Test
    void Should_ReturnEmptyNameList_When_NoBatteriesInRange() {
        when(batteryRepository.findByPostcodeIsBetween(START_VALUE, END_VALUE)).thenReturn(List.of());

        BatteryListResponseDto batteryList = batteryService.getBatteriesInPostalCodeRange(START_VALUE, END_VALUE);
        assertEquals(List.of(), batteryList.getBatteries());
        assertEquals(0, batteryList.getBatterySummary().getTotalBatteries());
        assertEquals(0, batteryList.getBatterySummary().getTotalWatts());
        assertEquals(0, batteryList.getBatterySummary().getAverageWatts());
    }

    @Test
    void Should_ThrowPowerPlantApplicationException_When_DataAccessExceptionIsThrownWhileGettingData() {
        when(batteryRepository.findByPostcodeIsBetween(START_VALUE, END_VALUE))
                .thenThrow(new DataAccessException("Failed") {
                });
        PowerPlantApplicationException exception = assertThrows(PowerPlantApplicationException.class, () -> {
            batteryService.getBatteriesInPostalCodeRange(START_VALUE, END_VALUE);
        });
        assertEquals("Failed to get batteries in postal code range.", exception.getMessage());
    }
}