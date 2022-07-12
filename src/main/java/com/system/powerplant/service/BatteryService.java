package com.system.powerplant.service;

import com.system.powerplant.domain.entity.Battery;
import com.system.powerplant.domain.request.BatteryRequestDto;
import com.system.powerplant.exception.PowerPlantApplicationException;
import com.system.powerplant.repository.BatteryRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            List<Battery> batteryList = new ArrayList<>();
            batteryRequestDtoList.forEach(batteryRequestDto -> batteryList.add(new Battery(batteryRequestDto)));
            batteryRepository.saveAll(batteryList);
        } catch (DataAccessException e) {
            throw new PowerPlantApplicationException("Failed to save battery detail list to database.", e);
        }
    }

    /**
     * Used to get battery details within the postcode range.
     *
     * @param startValue postcode start value
     * @param endValue   postcode end value
     * @return battery list.
     */
    public List<Battery> getBatteriesInPostalCodeRange(int startValue, int endValue) {
        try {
            Sort sort = Sort.by(Sort.Direction.ASC, "name");
            return batteryRepository.findByPostcodeIsBetween(startValue, endValue, sort);
        } catch (DataAccessException e) {
            throw new PowerPlantApplicationException("Failed to get batteries in postal code range.", e);
        }
    }
}
