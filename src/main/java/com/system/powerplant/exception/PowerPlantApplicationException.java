package com.system.powerplant.exception;

/**
 * Power plant application exception.
 */
public class PowerPlantApplicationException extends RuntimeException {

    /**
     * Authentication Exception with error message and throwable error.
     *
     * @param errorMessage error message
     * @param error        error
     */
    public PowerPlantApplicationException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }

    /**
     * Authentication Exception with error message.
     *
     * @param errorMessage error message
     */
    public PowerPlantApplicationException(String errorMessage) {
        super(errorMessage);
    }
}
