package com.system.powerplant.domain.response;

import com.system.powerplant.domain.entity.Battery;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Battery list response dto.
 */
@Getter
@Setter
public class BatteryListResponseDto implements ResponseDto {

    private int totalBatteries;
    private double totalWatts;
    private double averageWatts;
    private List<String> batteries = new ArrayList<>();

    public BatteryListResponseDto(List<Battery> batteryList) {
        this.totalBatteries = batteryList.size();
        batteryList.forEach(battery -> {
            this.batteries.add(battery.getName());
            this.totalWatts += battery.getWatts();
        });
        this.averageWatts = totalBatteries == 0 ? 0 : totalWatts / totalBatteries;
    }
}
