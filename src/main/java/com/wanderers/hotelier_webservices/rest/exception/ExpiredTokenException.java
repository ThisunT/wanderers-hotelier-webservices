package com.wanderers.hotelier_webservices.rest.exception;

public class ExpiredTokenException extends RESTException {

    private static final long serialVersionUID = 1L;

    /**
     * Class constructor
     *
     * @param message String value of Exception
     */
    public ExpiredTokenException(String message) {
        super(message);
    }
}
