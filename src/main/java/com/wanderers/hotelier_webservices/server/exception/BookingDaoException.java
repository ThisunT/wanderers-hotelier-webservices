package com.wanderers.hotelier_webservices.server.exception;

import com.wanderers.hotelier_webservices.server.dao.BookingDao;

/**
 * Custom Exception class which used to throw failures in {@link BookingDao}
 */
public class BookingDaoException extends ServerException {

    private static final long serialVersionUID = 1L;

    /**
     * Class constructor
     *
     * @param message String value of Exception
     */
    public BookingDaoException(String message, Throwable t) {
        super(message, t);
    }
}
