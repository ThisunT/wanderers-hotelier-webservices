package com.wanderers.hotelier_webservices.rest.delegate

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest

// Mark the class as a Spring component
@Component
abstract class AbstractApiDelegate @Autowired constructor(private val servletRequest: HttpServletRequest?) {

    protected fun getServletRequest(): HttpServletRequest {
        return Optional.ofNullable(servletRequest).orElseThrow {
            IllegalStateException("Request object is required")
        }
    }
}
