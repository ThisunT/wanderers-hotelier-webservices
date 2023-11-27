package com.wanderers.hotelier_webservices.rest.exception.handler

import com.wanderers.hotelier_webservices.rest.exception.ExpiredTokenException
import com.wanderers.hotelier_webservices.rest.exception.RESTException
import com.wanderers.hotelier_webservices.rest.exception.ResourceNotFoundException
import com.wanderers.hotelier_webservices.rest.exception.UnauthorizedHotelierException
import com.wanderers.hotelier_webservices.rest.exception.handler.ExceptionResponseEntityProvider.getExceptionResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*

@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {
    public override fun handleMissingServletRequestParameter(
        ex: MissingServletRequestParameterException,
        headers: HttpHeaders, status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return getExceptionResponse(HttpStatus.BAD_REQUEST, ex.localizedMessage)
    }

    /**
     * Handles invalidation of invalid value formats. Ex: rating = "5.5"
     */
    public override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException, headers: HttpHeaders,
        status: HttpStatus, request: WebRequest
    ): ResponseEntity<Any> {
        val exceptionSplit: Array<String> =
            ex.mostSpecificCause.localizedMessage.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
        val errorMsg: String = if (exceptionSplit.size > 1) {
            exceptionSplit[0] + exceptionSplit[exceptionSplit.size - 1]
        } else {
            ex.mostSpecificCause.localizedMessage
        }
        return getExceptionResponse(HttpStatus.BAD_REQUEST, errorMsg)
    }

    /**
     * Handles invalidation of field constraints. Ex: rating > 5
     */
    public override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException, headers: HttpHeaders,
        status: HttpStatus, request: WebRequest
    ): ResponseEntity<Any> {
        val fieldError = Optional.ofNullable(ex.bindingResult.fieldError).orElseThrow {
            RuntimeException(
                "no field errors present in the MethodArgumentNotValidException"
            )
        }
        val errMsg: String = if (fieldError.field == "image") {
            "image must be a valid URL, " + "invalid value " + fieldError.rejectedValue
        } else {
            fieldError.field + " " + fieldError.defaultMessage + ", " + "invalid value " + fieldError.rejectedValue
        }
        return getExceptionResponse(HttpStatus.BAD_REQUEST, errMsg)
    }

    @ExceptionHandler(RESTException::class)
    fun handleRequestException(ex: RESTException): ResponseEntity<Any> {
        return getExceptionResponse(HttpStatus.BAD_REQUEST, ex.localizedMessage)
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleHotelierIdException(ex: ResourceNotFoundException): ResponseEntity<Any> {
        return getExceptionResponse(HttpStatus.NOT_FOUND, ex.localizedMessage)
    }

    @ExceptionHandler(UnauthorizedHotelierException::class)
    fun handleUnauthorizedHotelier(ex: UnauthorizedHotelierException): ResponseEntity<Any> {
        return getExceptionResponse(HttpStatus.UNAUTHORIZED, ex.localizedMessage)
    }

    @ExceptionHandler(ExpiredTokenException::class)
    fun handleExpiredToken(ex: ExpiredTokenException): ResponseEntity<Any> {
        return getExceptionResponse(HttpStatus.FORBIDDEN, ex.localizedMessage)
    }
}