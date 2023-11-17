package com.wanderers.hotelier_webservices.rest.exception.handler

import com.wanderers.hotelier_webservices.rest.exception.ExpiredTokenException
import com.wanderers.hotelier_webservices.rest.exception.RESTException
import com.wanderers.hotelier_webservices.rest.exception.ResourceNotFoundException
import com.wanderers.hotelier_webservices.rest.exception.UnauthorizedHotelierException
import com.wanderers.hotelier_webservices.rest.model.ExceptionResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
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
        val response: Unit = ExceptionResponse()
            .error(HttpStatus.BAD_REQUEST.reasonPhrase)
            .message(ex.localizedMessage)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
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
        val errorMsg: String
        errorMsg = if (exceptionSplit.size > 1) {
            exceptionSplit[0] + exceptionSplit[exceptionSplit.size - 1]
        } else {
            ex.mostSpecificCause.localizedMessage
        }
        val response: Unit = ExceptionResponse()
            .error(HttpStatus.BAD_REQUEST.reasonPhrase)
            .message(errorMsg)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
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
        val errMsg: String
        errMsg = if (fieldError.field == "image") {
            "image must be a valid URL, " + "invalid value " + fieldError.rejectedValue
        } else {
            fieldError.field + " " + fieldError.defaultMessage + ", " + "invalid value " + fieldError.rejectedValue
        }
        val response: Unit = ExceptionResponse()
            .error(HttpStatus.BAD_REQUEST.reasonPhrase)
            .message(errMsg)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(RESTException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleRequestException(ex: RESTException): ResponseEntity<Any> {
        val response: Unit = ExceptionResponse()
            .error(HttpStatus.BAD_REQUEST.reasonPhrase)
            .message(ex.message)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleHotelierIdException(ex: ResourceNotFoundException): ResponseEntity<Any> {
        val response: Unit = ExceptionResponse()
            .error(HttpStatus.NOT_FOUND.reasonPhrase)
            .message(ex.message)
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(UnauthorizedHotelierException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    fun handleUnauthorizedHotelier(ex: UnauthorizedHotelierException): ResponseEntity<ExceptionResponse> {
        val response: Unit = ExceptionResponse()
            .error(HttpStatus.UNAUTHORIZED.reasonPhrase)
            .message(ex.localizedMessage)
        return ResponseEntity(response, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(ExpiredTokenException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    fun handleExpiredToken(ex: ExpiredTokenException): ResponseEntity<ExceptionResponse> {
        val response: Unit = ExceptionResponse()
            .error(HttpStatus.FORBIDDEN.reasonPhrase)
            .message(ex.localizedMessage)
        return ResponseEntity(response, HttpStatus.FORBIDDEN)
    }
}