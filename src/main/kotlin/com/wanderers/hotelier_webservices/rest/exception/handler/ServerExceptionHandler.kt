package com.wanderers.hotelier_webservices.rest.exception.handler

import com.wanderers.hotelier_webservices.server.exception.BookingUnavailableException
import com.wanderers.hotelier_webservices.server.exception.ResultNotFoundException
import com.wanderers.hotelier_webservices.server.exception.ServerException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ServerExceptionHandler {
    @ExceptionHandler(ServerException::class)
    fun handleServerException(ex: ServerException): ResponseEntity<Any> {
        return getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.localizedMessage)
    }

    @ExceptionHandler(ResultNotFoundException::class)
    fun handleServerNotFoundException(ex: ResultNotFoundException): ResponseEntity<Any> {
        return getExceptionResponse(HttpStatus.NOT_FOUND, ex.localizedMessage)
    }

    @ExceptionHandler(BookingUnavailableException::class)
    fun handleBookingUnavailableException(ex: BookingUnavailableException): ResponseEntity<Any> {
        return getExceptionResponse(HttpStatus.NOT_FOUND, ex.localizedMessage)
    }
}