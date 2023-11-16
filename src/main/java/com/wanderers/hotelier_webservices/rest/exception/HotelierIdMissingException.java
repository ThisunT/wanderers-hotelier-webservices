package com.wanderers.hotelier_webservices.rest.exception;

public class HotelierIdMissingException extends RESTException {

    private static final long serialVersionUID = 1L;

    /**
     * Class constructor
     *
     * @param message String value of Exception
     */
    public HotelierIdMissingException(String message) {
        super(message);
    }
}
