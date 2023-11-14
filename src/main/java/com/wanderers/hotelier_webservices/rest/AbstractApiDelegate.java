package com.wanderers.hotelier_webservices.rest;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Class is responsible for encapsulating common attributes of request delegates.
 */
public abstract class AbstractApiDelegate {

    private final HttpServletRequest servletRequest;

    @Autowired
    AbstractApiDelegate(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    protected HttpServletRequest getServletRequest() {
        return Optional.ofNullable(servletRequest).orElseThrow(() -> new IllegalStateException("request is required"));
    }

}
