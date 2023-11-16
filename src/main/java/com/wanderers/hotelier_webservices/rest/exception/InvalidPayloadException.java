package com.wanderers.hotelier_webservices.rest.exception;

public class InvalidPayloadException extends RESTException {

    private static final long serialVersionUID = 1L;

    /**
     * Class constructor
     *
     * @param message String value of Exception
     */
    public InvalidPayloadException(String message) {
        super(message);
    }
}
