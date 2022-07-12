package com.system.powerplant.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response wrapper.
 */
@Getter
@AllArgsConstructor
public class ResponseWrapper {

    private String message;
    private ResponseDto data;
}
