package com.wanderers.hotelier_webservices.rest.exception

class ResourceNotFoundException
/**
 * Class constructor
 *
 * @param message String value of Exception
 */
    (message: String?) : RuntimeException(message) {
    companion object {
        private const val serialVersionUID = 1L
    }
}