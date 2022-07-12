package com.system.powerplant.domain.entity;

import com.system.powerplant.domain.request.BatteryRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.UUID;

/**
 * Battery
 */
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Battery {

    @Transient
    private static final String BATTERY_ID_PREFIX = "bid-";

    @Id
    private String id;
    private String name;
    private int postcode;
    private double watts;

    public Battery(BatteryRequestDto batteryRequestDto) {
        this.id = BATTERY_ID_PREFIX + UUID.randomUUID();
        this.name = batteryRequestDto.getName();
        this.postcode = batteryRequestDto.getPostcode();
        this.watts = batteryRequestDto.getWatts();
    }
}
