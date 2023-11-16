package com.wanderers.hotelier_webservices.server.exception;

/**
 * Custom Exception class which used to throw when bookings are not available
 */
public class BookingUnavailableException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Class constructor
     *
     * @param message String value of Exception
     */
    public BookingUnavailableException(String message) {
        super(message);
    }
}
