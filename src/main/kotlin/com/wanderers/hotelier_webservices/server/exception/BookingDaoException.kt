package com.wanderers.hotelier_webservices.server.exception

import com.wanderers.hotelier_webservices.server.dao.BookingDao

/**
 * Custom Exception class which used to throw failures in [BookingDao]
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