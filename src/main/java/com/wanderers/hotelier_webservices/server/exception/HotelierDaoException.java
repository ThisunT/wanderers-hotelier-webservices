package com.wanderers.hotelier_webservices.server.exception;

import com.wanderers.hotelier_webservices.server.dao.impl.HotelierDaoImpl;

/**
 * Custom Exception class which used to throw failures in {@link HotelierDaoImpl}
 */
public class HotelierDaoException extends ServerException {

    private static final long serialVersionUID = 1L;

    /**
     * Class constructor
     *
     * @param message String value of Exception
     */
    public HotelierDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}