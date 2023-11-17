package com.wanderers.hotelier_webservices.server.exception

import com.wanderers.hotelier_webservices.server.service.AccommodationService

/**
 * Custom Exception class which used to throw failures in [AccommodationService]
 */
class AccommodationServiceException
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