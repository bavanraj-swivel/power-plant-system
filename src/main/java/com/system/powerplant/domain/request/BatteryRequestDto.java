package com.system.powerplant.domain.request;

import lombok.Getter;

/**
 * Battery request dto.
 */
@Getter
public class BatteryRequestDto {
    private String name;
    private int postcode;
    private double watts;
}
