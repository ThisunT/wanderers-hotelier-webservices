package com.wanderers.hotelier_webservices.server.exception;

import com.wanderers.hotelier_webservices.server.dao.impl.AccommodationDaoImpl;

/**
 * Custom Exception class which used to throw failures in {@link AccommodationDaoImpl}
 */
public class AccommodationDaoException extends ServerException {

    private static final long serialVersionUID = 1L;

    /**
     * Class constructor
     *
     * @param message String value of Exception
     */
    public AccommodationDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
