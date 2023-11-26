package com.wanderers.hotelier_webservices.server.exception;

import com.wanderers.hotelier_webservices.server.service.api.HotelierService;

/**
 * Custom Exception class which used to throw failures in {@link HotelierService}
 */
public class HotelierServiceException extends ServerException {

    private static final long serialVersionUID = 1L;

    /**
     * Class constructor
     *
     * @param message String value of Exception
     */
    public HotelierServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}