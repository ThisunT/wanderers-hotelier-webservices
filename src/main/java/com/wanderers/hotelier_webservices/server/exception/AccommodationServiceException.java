package com.wanderers.hotelier_webservices.server.exception;

import com.wanderers.hotelier_webservices.server.service.impl.AccommodationServiceImpl;

/**
 * Custom Exception class which used to throw failures in {@link AccommodationServiceImpl}
 */
public class AccommodationServiceException extends ServerException {

    private static final long serialVersionUID = 1L;

    /**
     * Class constructor
     *
     * @param message String value of Exception
     */
    public AccommodationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}