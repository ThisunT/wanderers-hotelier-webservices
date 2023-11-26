package com.wanderers.hotelier_webservices.server.exception

/**
 * Custom Exception class which used to throw when bookings are not available
 */
class BookingUnavailableException
/**
 * Class constructor
 *
 * @param message String value of Exception
 */
    (message: String?) : ServerException(message) {
    companion object {
        private const val serialVersionUID = 1L
    }
}