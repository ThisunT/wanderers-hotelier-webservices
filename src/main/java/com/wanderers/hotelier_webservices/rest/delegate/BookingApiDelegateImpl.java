package com.wanderers.hotelier_webservices.rest.delegate;

import com.wanderers.hotelier_webservices.rest.api.BookingApiDelegate;
import com.wanderers.hotelier_webservices.rest.exception.ResourceNotFoundException;
import com.wanderers.hotelier_webservices.rest.mapper.BookingMapper;
import com.wanderers.hotelier_webservices.rest.model.Booking;
import com.wanderers.hotelier_webservices.rest.validate.BookingValidator;
import com.wanderers.hotelier_webservices.server.dto.BookingDto;
import com.wanderers.hotelier_webservices.server.service.api.BookingService;
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
    private final BookingMapper bookingMapper;

    @Autowired
    BookingApiDelegateImpl(HttpServletRequest servletRequest, BookingValidator bookingValidator,
                           BookingService bookingService, BookingMapper bookingMapper) {
        super(servletRequest);
        this.bookingValidator = bookingValidator;
        this.bookingService = bookingService;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public ResponseEntity<Booking> submitBooking(Booking booking) {
        bookingValidator.validateBooking(booking, getToken());

        BookingDto bookingDto = bookingMapper.mapToBookingDto(booking);
        BookingDto createdBooking = bookingService.create(bookingDto);
        Booking bookingResponse = bookingMapper.mapToRestBooking(createdBooking);

        return new ResponseEntity<>(bookingResponse, HttpStatus.CREATED);
    }

    private String getToken() {
        return Optional.ofNullable(getServletRequest().getHeader("Is-Valid-Token"))
                .orElseThrow(() -> new ResourceNotFoundException("Is-Valid-Token header cannot be null"));
    }
}
