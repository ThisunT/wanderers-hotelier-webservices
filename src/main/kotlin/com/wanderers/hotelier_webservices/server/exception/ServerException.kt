package com.wanderers.hotelier_webservices.server.exception

open class ServerException : RuntimeException {
    /**
     * Class constructor
     *
     * @param message String value of Exception
     */
    constructor(message: String?) : super(message) {}
    constructor(message: String?, t: Throwable?) : super(message, t) {}

    companion object {
        private const val serialVersionUID = 1L
    }
}