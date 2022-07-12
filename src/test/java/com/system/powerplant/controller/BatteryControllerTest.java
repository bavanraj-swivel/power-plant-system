package com.system.powerplant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.powerplant.domain.entity.Battery;
import com.system.powerplant.domain.request.AddBulkBatteriesRequestDto;
import com.system.powerplant.domain.request.BatteryRequestDto;
import com.system.powerplant.enumeration.ErrorMessage;
import com.system.powerplant.enumeration.SuccessMessage;
import com.system.powerplant.exception.PowerPlantApplicationException;
import com.system.powerplant.service.BatteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class tests the {@link BatteryController } class.
 */
class BatteryControllerTest {

    private static final String ADD_BATTERY_URI = "/api/battery/add";
    private static final String GET_BATTERY_URI = "/api/battery/find-in-postal-code-range/start/{startValue}/end/{endValue}";
    private static final int START_VALUE = 0;
    private static final int END_VALUE = 10500;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AddBulkBatteriesRequestDto addBulkBatteriesRequestDto = getAddBulkBatteriesRequestDto();
    private MockMvc mockMvc;
    @Mock
    private BatteryService batteryService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        BatteryController batteryController = new BatteryController(batteryService);
        mockMvc = MockMvcBuilders.standaloneSetup(batteryController).build();
    }

    /**
     * Unit tests for add bulk battery details.
     */

    @Test
    void Should_ReturnSuccessMessage_When_AddingBatteryDetailsIsSuccessful() throws Exception {
        String objectToJson = objectMapper.writeValueAsString(addBulkBatteriesRequestDto);
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_BATTERY_URI)
                        .content(objectToJson)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessMessage.SUCCESSFULLY_ADDED.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnErrorMessage_When_AddingBatteryDetailsIsFailedDueToException() throws Exception {
        doThrow(new PowerPlantApplicationException("Failed to save battery detail list to database."))
                .when(batteryService).addBulkBatteries(anyList());

        String objectToJson = objectMapper.writeValueAsString(addBulkBatteriesRequestDto);
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_BATTERY_URI)
                        .content(objectToJson)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ErrorMessage.FAILED_TO_ADD.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Unit tests for get battery details.
     */

    @Test
    void Should_ReturnSuccessMessage_When_GettingBatteryDetailsInPostcodeRangeIsSuccessful() throws Exception {
        String uri = GET_BATTERY_URI
                .replace("{startValue}", String.valueOf(START_VALUE))
                .replace("{endValue}", String.valueOf(END_VALUE));
        List<Battery> batteryList = new ArrayList<>();
        batteryList.add(new Battery());
        when(batteryService.getBatteriesInPostalCodeRange(START_VALUE, END_VALUE)).thenReturn(batteryList);

        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessMessage.SUCCESSFULLY_RETURNED.getMessage()))
                .andExpect(jsonPath("$.data.totalBatteries").value(1));
    }

    @Test
    void Should_ReturnErrorMessage_When_GettingBatteryDetailsInInvalidPostcodeRange() throws Exception {
        String uri = GET_BATTERY_URI
                .replace("{startValue}", String.valueOf(END_VALUE))
                .replace("{endValue}", String.valueOf(START_VALUE));

        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INVALID_VALUES.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnErrorMessage_When_GettingBatteryDetailsInPostcodeRangeFailedDueToExceptions() throws Exception {
        String uri = GET_BATTERY_URI
                .replace("{startValue}", String.valueOf(START_VALUE))
                .replace("{endValue}", String.valueOf(END_VALUE));
        doThrow(new PowerPlantApplicationException("Failed to get batteries in postal code range."))
                .when(batteryService).getBatteriesInPostalCodeRange(START_VALUE, END_VALUE);

        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ErrorMessage.FAILED_TO_GET.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    private AddBulkBatteriesRequestDto getAddBulkBatteriesRequestDto() {
        AddBulkBatteriesRequestDto addBulkBatteriesRequestDto = new AddBulkBatteriesRequestDto();
        List<BatteryRequestDto> batteryList = new ArrayList<>();
        batteryList.add(new BatteryRequestDto());
        addBulkBatteriesRequestDto.setBatteryList(batteryList);
        return addBulkBatteriesRequestDto;
    }
}