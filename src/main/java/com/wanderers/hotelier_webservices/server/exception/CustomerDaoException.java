package com.wanderers.hotelier_webservices.server.exception;

import com.wanderers.hotelier_webservices.server.dao.impl.CustomerDaoImpl;

/**
 * Custom Exception class which used to throw failures in {@link CustomerDaoImpl}
 */
public class CustomerDaoException extends ServerException {

    private static final long serialVersionUID = 1L;

    /**
     * Class constructor
     *
     * @param message String value of Exception
     */
    public CustomerDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
