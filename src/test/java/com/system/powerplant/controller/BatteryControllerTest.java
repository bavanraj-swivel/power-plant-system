package com.system.powerplant.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.powerplant.domain.request.BatteryRequestDto;
import com.system.powerplant.domain.response.BatteryListResponseDto;
import com.system.powerplant.enumeration.ErrorMessage;
import com.system.powerplant.enumeration.SuccessMessage;
import com.system.powerplant.exception.PowerPlantApplicationException;
import com.system.powerplant.exception.RequiredFieldsMissingException;
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
    void Should_ReturnOk_When_AddingBatteryDetailsIsSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_BATTERY_URI)
                        .content(getBatteryRequestDtoList())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessMessage.SUCCESSFULLY_ADDED.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnBadRequest_When_AddingBatteryDetailsIsFailedDueToMissingFields() throws Exception {
        doThrow(new RequiredFieldsMissingException("Required fields are missing."))
                .when(batteryService).addBulkBatteries(anyList());
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_BATTERY_URI)
                        .content(getBatteryRequestDtoList())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorMessage.MISSING_REQUIRED_FIELDS.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnInternalServerError_When_AddingBatteryDetailsIsFailedDueToInternalErrors() throws Exception {
        doThrow(new PowerPlantApplicationException("Failed to save battery detail list to database."))
                .when(batteryService).addBulkBatteries(anyList());
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_BATTERY_URI)
                        .content(getBatteryRequestDtoList())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Unit tests for get battery details.
     */

    @Test
    void Should_ReturnOk_When_GettingBatteryDetailsInPostcodeRangeIsSuccessful() throws Exception {
        String uri = GET_BATTERY_URI
                .replace("{startValue}", String.valueOf(START_VALUE))
                .replace("{endValue}", String.valueOf(END_VALUE));
        when(batteryService.getBatteriesInPostalCodeRange(START_VALUE, END_VALUE))
                .thenReturn(new BatteryListResponseDto());
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessMessage.SUCCESSFULLY_RETURNED.getMessage()));
    }

    @Test
    void Should_ReturnBadRequest_When_GettingBatteryDetailsInInvalidPostcodeRange() throws Exception {
        String uri = GET_BATTERY_URI
                .replace("{startValue}", String.valueOf(END_VALUE))
                .replace("{endValue}", String.valueOf(START_VALUE));
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INVALID_VALUES.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnInternalServerError_When_GettingBatteryDetailsIsFailedDueToInternalErrors() throws Exception {
        String uri = GET_BATTERY_URI
                .replace("{startValue}", String.valueOf(START_VALUE))
                .replace("{endValue}", String.valueOf(END_VALUE));
        doThrow(new PowerPlantApplicationException("Failed to get batteries in postal code range."))
                .when(batteryService).getBatteriesInPostalCodeRange(START_VALUE, END_VALUE);
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    private String getBatteryRequestDtoList() throws JsonProcessingException {
        List<BatteryRequestDto> batteryList = new ArrayList<>();
        batteryList.add(new BatteryRequestDto("CEB", 10400, 500.0));
        batteryList.add(new BatteryRequestDto("LECO", 10800, 300.0));
        return objectMapper.writeValueAsString(batteryList);
    }
}