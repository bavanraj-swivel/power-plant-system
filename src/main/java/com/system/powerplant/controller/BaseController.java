package com.system.powerplant.controller;

import com.system.powerplant.domain.response.ResponseWrapper;
import com.system.powerplant.domain.response.ResponseDto;
import com.system.powerplant.enumeration.ErrorMessage;
import com.system.powerplant.enumeration.SuccessMessage;

/**
 * Base controller.
 */
public class BaseController {

    /**
     * This method is used to get success response.
     *
     * @param successMessage success message
     * @param responseDto    response data
     * @return success response.
     */
    protected ResponseWrapper getSuccessResponse(SuccessMessage successMessage, ResponseDto responseDto) {
        return new ResponseWrapper(successMessage.getMessage(), responseDto);
    }

    /**
     * This method is used to get error response.
     *
     * @param errorMessage error message
     * @return error response.
     */
    protected ResponseWrapper getErrorResponse(ErrorMessage errorMessage) {
        return new ResponseWrapper(errorMessage.getMessage(), null);
    }
}
