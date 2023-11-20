package com.wanderers.hotelier_webservices.rest.exception.handler;

import com.wanderers.hotelier_webservices.rest.exception.ExpiredTokenException;
import com.wanderers.hotelier_webservices.rest.exception.ResourceNotFoundException;
import com.wanderers.hotelier_webservices.rest.exception.RESTException;
import com.wanderers.hotelier_webservices.rest.exception.UnauthorizedHotelierException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

import static com.wanderers.hotelier_webservices.rest.exception.handler.ExceptionResponseEntityProvider.getExceptionResponse;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                       HttpHeaders headers, HttpStatus status,
                                                                       WebRequest request) {
        return getExceptionResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }

    /**
     * Handles invalidation of invalid value formats. Ex: rating = "5.5"
     */
    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {

        String[] exceptionSplit = ex.getMostSpecificCause().getLocalizedMessage().split(":");
        String errorMsg;

        if (exceptionSplit.length > 1) {
            errorMsg = exceptionSplit[0] + exceptionSplit[exceptionSplit.length -1];
        } else {
            errorMsg = ex.getMostSpecificCause().getLocalizedMessage();
        }

        return getExceptionResponse(HttpStatus.BAD_REQUEST, errorMsg);
    }

    /**
     * Handles invalidation of field constraints. Ex: rating > 5
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {

        FieldError fieldError = Optional.ofNullable(ex.getBindingResult().getFieldError()).orElseThrow(() ->
                new RuntimeException("no field errors present in the MethodArgumentNotValidException"));

        String errMsg;

        if (fieldError.getField().equals("image")) {
            errMsg = "image must be a valid URL, " + "invalid value " + fieldError.getRejectedValue();
        } else {
            errMsg = fieldError.getField()  + " " + fieldError.getDefaultMessage() + ", " + "invalid value " + fieldError.getRejectedValue();
        }

        return getExceptionResponse(HttpStatus.BAD_REQUEST, errMsg);
    }

    @ExceptionHandler(RESTException.class)
    public ResponseEntity<Object> handleRequestException(RESTException ex) {
        return getExceptionResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleHotelierIdException(ResourceNotFoundException ex) {
        return getExceptionResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());

    }

    @ExceptionHandler(UnauthorizedHotelierException.class)
    public ResponseEntity<Object> handleUnauthorizedHotelier(UnauthorizedHotelierException ex) {
        return getExceptionResponse(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage());

    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<Object> handleExpiredToken(ExpiredTokenException ex) {
        return getExceptionResponse(HttpStatus.FORBIDDEN, ex.getLocalizedMessage());
    }

}
