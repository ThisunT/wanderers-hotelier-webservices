package com.wanderers.hotelier_webservices.rest.delegate

import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * Class is responsible for encapsulating common attributes of request delegates.
 */
abstract class AbstractApiDelegate @Autowired internal constructor(private val servletRequest: HttpServletRequest) {
    protected fun getServletRequest(): HttpServletRequest {
        return Optional.ofNullable(servletRequest).orElseThrow {
            IllegalStateException(
                "request object is required"
            )
        }
    }
}