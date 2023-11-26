package com.wanderers.hotelier_webservices.server.exception;

import com.wanderers.hotelier_webservices.server.service.impl.BookingServiceImpl;

/**
 * Custom Exception class which used to throw failures in {@link BookingServiceImpl}
 */
public class BookingServiceException extends ServerException {

    private static final long serialVersionUID = 1L;

    /**
     * Class constructor
     *
     * @param message String value of Exception
     */
    public BookingServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
