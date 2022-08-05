package com.system.powerplant.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.DoubleSummaryStatistics;

/**
 * Battery summary response dto
 */
@Getter
@Setter
public class BatterySummaryResponseDto implements ResponseDto {

    private long totalBatteries;
    private double totalWatts;
    private double averageWatts;

    public BatterySummaryResponseDto(DoubleSummaryStatistics doubleSummaryStatistics) {
        this.totalBatteries = doubleSummaryStatistics.getCount();
        this.totalWatts = doubleSummaryStatistics.getSum();
        this.averageWatts = doubleSummaryStatistics.getAverage();
    }
}
