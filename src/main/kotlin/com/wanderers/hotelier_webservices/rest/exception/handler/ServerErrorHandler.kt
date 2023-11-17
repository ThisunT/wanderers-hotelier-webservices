package com.wanderers.hotelier_webservices.rest.exception.handler

import com.wanderers.hotelier_webservices.rest.model.ExceptionResponse
import com.wanderers.hotelier_webservices.server.exception.BookingUnavailableException
import com.wanderers.hotelier_webservices.server.exception.ResultNotFoundException
import com.wanderers.hotelier_webservices.server.exception.ServerException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

//@ControllerAdvice
class ServerErrorHandler {
    @ExceptionHandler(ServerException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleServerException(ex: ServerException): ResponseEntity<ExceptionResponse> {
        val response: Unit =
            ExceptionResponse().error(HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase).message(ex.localizedMessage)
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(ResultNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleServerNotFoundException(ex: ResultNotFoundException): ResponseEntity<ExceptionResponse> {
        val response: Unit = ExceptionResponse().error(HttpStatus.NOT_FOUND.reasonPhrase).message(ex.localizedMessage)
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(BookingUnavailableException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    fun handleBookingUnavailableException(ex: BookingUnavailableException): ResponseEntity<ExceptionResponse> {
        val response: Unit = ExceptionResponse().error(HttpStatus.CONFLICT.reasonPhrase).message(ex.localizedMessage)
        return ResponseEntity(response, HttpStatus.CONFLICT)
    }
}