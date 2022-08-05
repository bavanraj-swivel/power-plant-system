package com.system.powerplant.enumeration;

import lombok.Getter;

/**
 * Error messages.
 */
@Getter
public enum ErrorMessage {

    INTERNAL_SERVER_ERROR("Something went wrong."),
    INVALID_VALUES("Invalid postcode range."),
    MISSING_REQUIRED_FIELDS("Required fields are missing.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
