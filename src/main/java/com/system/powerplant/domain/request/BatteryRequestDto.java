package com.system.powerplant.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Battery request dto.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BatteryRequestDto implements RequestDto {

    private String name;
    private int postcode;
    private double watts;

    /**
     * This method checks if required fields are available.
     *
     * @return true/false.
     */
    @Override
    public boolean isRequiredFieldsAvailable() {
        return name != null && !name.isEmpty() && postcode > 0 && watts >= 0;
    }
}
