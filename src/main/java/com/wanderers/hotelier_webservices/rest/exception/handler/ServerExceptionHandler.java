package com.wanderers.hotelier_webservices.rest.exception.handler;

import com.wanderers.hotelier_webservices.server.exception.BookingUnavailableException;
import com.wanderers.hotelier_webservices.server.exception.ResultNotFoundException;
import com.wanderers.hotelier_webservices.server.exception.ServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.wanderers.hotelier_webservices.rest.exception.handler.ExceptionResponseEntityProvider.getExceptionResponse;

@ControllerAdvice
public class ServerExceptionHandler {

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<Object> handleServerException(ServerException ex) {
        return getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
    }

    @ExceptionHandler(ResultNotFoundException.class)
    public ResponseEntity<Object> handleServerNotFoundException(ResultNotFoundException ex) {
        return getExceptionResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler(BookingUnavailableException.class)
    public ResponseEntity<Object> handleBookingUnavailableException(BookingUnavailableException ex) {
        return getExceptionResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }
}
