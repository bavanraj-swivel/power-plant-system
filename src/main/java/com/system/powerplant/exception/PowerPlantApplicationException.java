package com.system.powerplant.exception;

/**
 * Power plant application exception.
 */
public class PowerPlantApplicationException extends RuntimeException {

    /**
     * Power plant application exception with error message and throwable error.
     *
     * @param errorMessage error message
     * @param error        error
     */
    public PowerPlantApplicationException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }

    /**
     * Power plant application exception with error message.
     *
     * @param errorMessage error message
     */
    public PowerPlantApplicationException(String errorMessage) {
        super(errorMessage);
    }
}
