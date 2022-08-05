package com.system.powerplant.controller;

import com.system.powerplant.domain.request.BatteryRequestDto;
import com.system.powerplant.domain.response.BatteryListResponseDto;
import com.system.powerplant.domain.response.ResponseWrapper;
import com.system.powerplant.enumeration.ErrorMessage;
import com.system.powerplant.enumeration.SuccessMessage;
import com.system.powerplant.exception.PowerPlantApplicationException;
import com.system.powerplant.exception.RequiredFieldsMissingException;
import com.system.powerplant.service.BatteryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Battery controller.
 */
@RestController
@Slf4j
@RequestMapping("/api/battery")
public class BatteryController extends BaseController {

    private final BatteryService batteryService;

    public BatteryController(BatteryService batteryService) {
        this.batteryService = batteryService;
    }

    /**
     * This method is used to add battery details.
     *
     * @param batteryRequestDtoList batteryRequestDtoList
     * @return success/error response.
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseWrapper> addBatteries(@RequestBody List<BatteryRequestDto> batteryRequestDtoList) {
        try {
            batteryService.addBulkBatteries(batteryRequestDtoList);
            log.debug("Successfully added battery details.");
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_ADDED, null);
        } catch (RequiredFieldsMissingException e) {
            log.error("Required fields are missing to add bulk battery details.", e);
            return getBadRequestErrorResponse(ErrorMessage.MISSING_REQUIRED_FIELDS);
        } catch (PowerPlantApplicationException e) {
            log.error("Failed to add bulk battery details.", e);
            return getInternalServerError();
        }
    }

    /**
     * This method is used to get battery details with statistics.
     *
     * @param startValue postcode start value
     * @param endValue   postcode end value
     * @return success/error response.
     */
    @GetMapping("/find-in-postal-code-range/start/{startValue}/end/{endValue}")
    public ResponseEntity<ResponseWrapper> findByPostalCodeRange(@PathVariable int startValue,
                                                                 @PathVariable int endValue) {
        try {
            if (endValue < startValue || startValue < 0)
                return getBadRequestErrorResponse(ErrorMessage.INVALID_VALUES);
            BatteryListResponseDto batteryListResponseDto =
                    batteryService.getBatteriesInPostalCodeRange(startValue, endValue);
            log.debug("Successfully returned battery details.");
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_RETURNED, batteryListResponseDto);
        } catch (PowerPlantApplicationException e) {
            log.error("Failed to get battery details.", e);
            return getInternalServerError();
        }
    }
}
