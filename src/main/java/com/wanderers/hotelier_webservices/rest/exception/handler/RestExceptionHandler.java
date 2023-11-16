package com.wanderers.hotelier_webservices.rest.exception.handler;

import com.wanderers.hotelier_webservices.rest.exception.ExpiredTokenException;
import com.wanderers.hotelier_webservices.rest.exception.ResourceNotFoundException;
import com.wanderers.hotelier_webservices.rest.exception.RESTException;
import com.wanderers.hotelier_webservices.rest.exception.UnauthorizedHotelierException;
import com.wanderers.hotelier_webservices.rest.model.ExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                       HttpHeaders headers, HttpStatus status,
                                                                       WebRequest request) {
        var response = new ExceptionResponse()
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getLocalizedMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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

        var response = new ExceptionResponse()
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(errorMsg);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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

        var response = new ExceptionResponse()
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(errMsg);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RESTException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Object> handleRequestException(RESTException ex) {
        var response = new ExceptionResponse()
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<Object> handleHotelierIdException(ResourceNotFoundException ex) {
        var response = new ExceptionResponse()
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedHotelierException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleUnauthorizedHotelier(UnauthorizedHotelierException ex) {
        var response = new ExceptionResponse()
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(ex.getLocalizedMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredTokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleExpiredToken(ExpiredTokenException ex) {
        var response = new ExceptionResponse()
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message(ex.getLocalizedMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
