package com.system.powerplant.controller;

import com.system.powerplant.domain.entity.Battery;
import com.system.powerplant.domain.request.AddBulkBatteriesRequestDto;
import com.system.powerplant.domain.response.BatteryListResponseDto;
import com.system.powerplant.domain.response.ResponseWrapper;
import com.system.powerplant.enumeration.ErrorMessage;
import com.system.powerplant.enumeration.SuccessMessage;
import com.system.powerplant.exception.PowerPlantApplicationException;
import com.system.powerplant.service.BatteryService;
import lombok.extern.slf4j.Slf4j;
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
     * @param addBulkBatteriesRequestDto addBulkBatteriesRequestDto
     * @return success/error response.
     */
    @PostMapping("/add")
    public ResponseWrapper addBatteries(@RequestBody AddBulkBatteriesRequestDto addBulkBatteriesRequestDto) {
        try {
            batteryService.addBulkBatteries(addBulkBatteriesRequestDto.getBatteryList());
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_ADDED, null);
        } catch (PowerPlantApplicationException e) {
            log.error("Failed to add bulk battery details.", e);
            return getErrorResponse(ErrorMessage.FAILED_TO_ADD);
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
    public ResponseWrapper findByPostalCodeRange(@PathVariable int startValue, @PathVariable int endValue) {
        try {
            if (endValue < startValue || startValue < 0)
                return getErrorResponse(ErrorMessage.INVALID_VALUES);
            List<Battery> batteryList = batteryService.getBatteriesInPostalCodeRange(startValue, endValue);
            BatteryListResponseDto batteryListResponseDto = new BatteryListResponseDto(batteryList);
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_RETURNED, batteryListResponseDto);
        } catch (PowerPlantApplicationException e) {
            log.error("Failed to get battery details.", e);
            return getErrorResponse(ErrorMessage.FAILED_TO_GET);
        }
    }
}
