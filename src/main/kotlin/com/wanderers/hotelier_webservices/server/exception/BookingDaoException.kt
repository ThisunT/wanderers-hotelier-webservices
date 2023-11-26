package com.wanderers.hotelier_webservices.server.exception

import com.wanderers.hotelier_webservices.server.dao.impl.BookingDaoImpl

/**
 * Custom Exception class which used to throw failures in [BookingDaoImpl]
 */
class BookingDaoException
/**
 * Class constructor
 *
 * @param message String value of Exception
 */
    (message: String?, t: Throwable?) :
    ServerException(message, t) {
    companion object {
        private const val serialVersionUID = 1L
    }
}