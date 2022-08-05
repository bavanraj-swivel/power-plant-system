package com.system.powerplant.domain.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.powerplant.exception.PowerPlantApplicationException;

/**
 * Response dto.
 */
public interface ResponseDto {

    /**
     * This method converts object to json string.
     *
     * @return json string
     */
    default String toLogJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new PowerPlantApplicationException("Object to json conversion was failed.", e);
        }
    }
}
