package com.system.powerplant.exception;

public class RequiredFieldsMissingException extends PowerPlantApplicationException {

    /**
     * Required fields missing exception with error message.
     *
     * @param errorMessage error message
     */
    public RequiredFieldsMissingException(String errorMessage) {
        super(errorMessage);
    }
}
