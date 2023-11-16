package com.wanderers.hotelier_webservices.server.exception;

public class ResultNotFoundException extends HotelierDaoException {

    private static final long serialVersionUID = 1L;

    /**
     * Class constructor
     *
     * @param message String value of Exception
     */
    public ResultNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
