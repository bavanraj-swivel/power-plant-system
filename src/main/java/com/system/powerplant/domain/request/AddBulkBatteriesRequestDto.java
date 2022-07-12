package com.system.powerplant.domain.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Add bulk batteries request dto.
 */
@Getter
@Setter
public class AddBulkBatteriesRequestDto {

    private List<BatteryRequestDto> batteryList;
}
