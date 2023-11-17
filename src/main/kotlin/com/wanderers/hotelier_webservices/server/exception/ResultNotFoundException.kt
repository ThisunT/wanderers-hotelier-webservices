package com.wanderers.hotelier_webservices.server.exception

class ResultNotFoundException
/**
 * Class constructor
 *
 * @param message String value of Exception
 */
    (message: String?, cause: Throwable?) : HotelierDaoException(message, cause) {
    companion object {
        private const val serialVersionUID = 1L
    }
}