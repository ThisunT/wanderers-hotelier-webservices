package com.wanderers.hotelier_webservices.server.exception

import com.wanderers.hotelier_webservices.server.dao.impl.CustomerDaoImpl

/**
 * Custom Exception class which used to throw failures in [CustomerDaoImpl]
 */
class CustomerDaoException
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