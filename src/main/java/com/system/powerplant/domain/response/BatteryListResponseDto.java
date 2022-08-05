package com.system.powerplant.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Battery list response dto.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BatteryListResponseDto implements ResponseDto {

    private BatterySummaryResponseDto batterySummary;
    private List<String> batteries;
}
