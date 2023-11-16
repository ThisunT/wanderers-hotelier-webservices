package com.wanderers.hotelier_webservices.rest.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Class constructor
     *
     * @param message String value of Exception
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
