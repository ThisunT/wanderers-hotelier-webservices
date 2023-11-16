package com.wanderers.hotelier_webservices.rest.exception.handler;

import com.wanderers.hotelier_webservices.rest.exception.InvalidHotelierException;
import com.wanderers.hotelier_webservices.rest.model.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller advice class to handle exceptions thrown because the requested resource is unauthorized.
 * Each unauthorized request is responded with {@link ExceptionResponse} object.
 */
@ControllerAdvice
public class UnauthorizedRequestHandler {

    @ExceptionHandler(InvalidHotelierException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleTPIDMismatchException(InvalidHotelierException ex) {
        var response = new ExceptionResponse().error(HttpStatus.UNAUTHORIZED.getReasonPhrase()).message(ex.getLocalizedMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

}
