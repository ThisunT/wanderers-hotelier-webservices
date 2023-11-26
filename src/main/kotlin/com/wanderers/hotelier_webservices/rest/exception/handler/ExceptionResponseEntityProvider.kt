package com.wanderers.hotelier_webservices.rest.exception.handler

import com.wanderers.hotelier_webservices.rest.model.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ResponseBody

object ExceptionResponseEntityProvider {
    @ResponseBody
    fun getExceptionResponse(httpStatus: HttpStatus, message: String?): ResponseEntity<Any> {
        val response: Unit = ExceptionResponse().error(httpStatus.reasonPhrase).message(message)
        return ResponseEntity(response, httpStatus)
    }
}