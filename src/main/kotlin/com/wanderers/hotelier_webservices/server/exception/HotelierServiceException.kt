package com.wanderers.hotelier_webservices.server.exception

import com.wanderers.hotelier_webservices.server.service.api.HotelierService

/**
 * Custom Exception class which used to throw failures in [HotelierService]
 */
class HotelierServiceException
/**
 * Class constructor
 *
 * @param message String value of Exception
 */
    (message: String?, cause: Throwable?) :
    ServerException(message, cause) {
    companion object {
        private const val serialVersionUID = 1L
    }
}