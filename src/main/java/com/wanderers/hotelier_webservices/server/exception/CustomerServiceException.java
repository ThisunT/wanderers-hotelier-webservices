package com.wanderers.hotelier_webservices.server.exception;

import com.wanderers.hotelier_webservices.server.service.api.CustomerService;

/**
 * Custom Exception class which used to throw failures in {@link CustomerService}
 */
public class CustomerServiceException extends ServerException {

    private static final long serialVersionUID = 1L;

    /**
     * Class constructor
     *
     * @param message String value of Exception
     */
    public CustomerServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
