package com.wanderers.hotelier_webservices.rest.exception.handler;

import com.wanderers.hotelier_webservices.rest.model.ExceptionResponse;
import com.wanderers.hotelier_webservices.server.exception.ServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ServerErrorHandler {

    @ExceptionHandler(ServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleServerException(ServerException ex) {
        var response = new ExceptionResponse().error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).message(ex.getLocalizedMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
