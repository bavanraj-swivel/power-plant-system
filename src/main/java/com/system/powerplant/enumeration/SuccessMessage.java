package com.system.powerplant.enumeration;

import lombok.Getter;

/**
 * Success messages.
 */
@Getter
public enum SuccessMessage {

    SUCCESSFULLY_ADDED("Successfully added battery details."),
    SUCCESSFULLY_RETURNED("Successfully returned battery details.");

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
