package com.wanderers.hotelier_webservices.rest.exception.handler;

import com.wanderers.hotelier_webservices.rest.model.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

public class ExceptionResponseEntityProvider {

    private ExceptionResponseEntityProvider() {
    }

    @ResponseBody
    public static ResponseEntity<Object> getExceptionResponse(HttpStatus httpStatus, String message) {
        var response = new ExceptionResponse().error(httpStatus.getReasonPhrase()).message(message);
        return new ResponseEntity<>(response, httpStatus);
    }

}
