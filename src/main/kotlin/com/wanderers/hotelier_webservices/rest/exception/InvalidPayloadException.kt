package com.wanderers.hotelier_webservices.rest.exception

class InvalidPayloadException
/**
 * Class constructor
 *
 * @param message String value of Exception
 */
    (message: String?) : RESTException(message) {
    companion object {
        private const val serialVersionUID = 1L
    }
}