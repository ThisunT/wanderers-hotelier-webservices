package com.wanderers.hotelier_webservices.rest.delegate;

import com.wanderers.hotelier_webservices.rest.api.BookingApiDelegate;
import com.wanderers.hotelier_webservices.rest.exception.ExpiredTokenException;
import com.wanderers.hotelier_webservices.rest.exception.ResourceNotFoundException;
import com.wanderers.hotelier_webservices.rest.model.Booking;
import com.wanderers.hotelier_webservices.rest.validate.BookingValidator;
import com.wanderers.hotelier_webservices.server.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Class implements the endpoints of booking resource.
 */
@Service("booking_api_delegate")
public class BookingApiDelegateImpl extends AbstractApiDelegate implements BookingApiDelegate {

    private final BookingValidator bookingValidator;
    private final BookingService bookingService;

    @Autowired
    BookingApiDelegateImpl(HttpServletRequest servletRequest, BookingValidator bookingValidator, BookingService bookingService) {
        super(servletRequest);
        this.bookingValidator = bookingValidator;
        this.bookingService = bookingService;
    }

    @Override
    public ResponseEntity<Booking> submitBooking(Booking booking) {
        bookingValidator.validateBooking(booking, getToken());

        Booking createdBooking = bookingService.create(booking);

        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    private String getToken() {
        return Optional.ofNullable(getServletRequest().getHeader("Is-Valid-Token"))
                .orElseThrow(() -> new ResourceNotFoundException("Is-Valid-Token header cannot be null"));
    }
}
