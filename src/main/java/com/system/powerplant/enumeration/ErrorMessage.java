package com.system.powerplant.enumeration;

import lombok.Getter;

/**
 * Error messages.
 */
@Getter
public enum ErrorMessage {

    FAILED_TO_ADD("Failed to add battery details."),
    FAILED_TO_GET("Failed to get battery details."),
    INVALID_VALUES("Invalid postcode range.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
