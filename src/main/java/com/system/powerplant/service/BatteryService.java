package com.system.powerplant.service;

import com.system.powerplant.domain.entity.Battery;
import com.system.powerplant.domain.request.BatteryRequestDto;
import com.system.powerplant.domain.response.BatteryListResponseDto;
import com.system.powerplant.domain.response.BatterySummaryResponseDto;
import com.system.powerplant.exception.PowerPlantApplicationException;
import com.system.powerplant.exception.RequiredFieldsMissingException;
import com.system.powerplant.repository.BatteryRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Battery service.
 */
@Service
public class BatteryService {

    private final BatteryRepository batteryRepository;

    public BatteryService(BatteryRepository batteryRepository) {
        this.batteryRepository = batteryRepository;
    }

    /**
     * Used to add bulk battery details.
     *
     * @param batteryRequestDtoList batteryRequestDtoList
     */
    public void addBulkBatteries(List<BatteryRequestDto> batteryRequestDtoList) {
        try {
            List<Battery> batteryList = batteryRequestDtoList
                    .stream()
                    .map(this::validateRequiredFieldsForBatteries)
                    .collect(Collectors.toList());
            batteryRepository.saveAll(batteryList);
        } catch (DataAccessException e) {
            throw new PowerPlantApplicationException("Failed to save battery detail list to database.", e);
        }
    }

    /**
     * This method checks if required fields are available.
     *
     * @param batteryRequestDto batteryRequestDto
     * @return battery/exception.
     */
    private Battery validateRequiredFieldsForBatteries(BatteryRequestDto batteryRequestDto) {
        if (!batteryRequestDto.isRequiredFieldsAvailable()) {
            throw new RequiredFieldsMissingException("Required fields are missing for battery request: " +
                    batteryRequestDto.toLogJson());
        }
        return new Battery(batteryRequestDto);
    }

    /**
     * Used to get battery details within the postcode range.
     *
     * @param startValue postcode start value
     * @param endValue   postcode end value
     * @return battery list.
     */
    public BatteryListResponseDto getBatteriesInPostalCodeRange(int startValue, int endValue) {
        try {
            List<Battery> batteryList = batteryRepository.findByPostcodeIsBetween(startValue, endValue);

            List<String> batteryNames = batteryList
                    .stream()
                    .map(Battery::getName)
                    .sorted()
                    .collect(Collectors.toList());

            DoubleSummaryStatistics doubleSummaryStatistics = batteryList
                    .stream()
                    .mapToDouble(Battery::getWatts)
                    .summaryStatistics();

            BatterySummaryResponseDto batterySummary = new BatterySummaryResponseDto(doubleSummaryStatistics);
            return new BatteryListResponseDto(batterySummary, batteryNames);
        } catch (DataAccessException e) {
            throw new PowerPlantApplicationException("Failed to get batteries in postal code range.", e);
        }
    }
}
