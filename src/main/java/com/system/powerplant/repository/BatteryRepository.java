package com.system.powerplant.repository;

import com.system.powerplant.domain.entity.Battery;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Battery repository.
 */
public interface BatteryRepository extends JpaRepository<Battery, String> {

    /**
     * Used to query battery details within the postcode range.
     *
     * @param startValue postcode start value
     * @param endValue   postcode end value
     * @param sort       sorting type
     * @return battery list.
     */
    List<Battery> findByPostcodeIsBetween(int startValue, int endValue, Sort sort);
}
