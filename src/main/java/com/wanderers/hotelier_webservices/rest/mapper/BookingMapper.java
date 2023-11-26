package com.wanderers.hotelier_webservices.rest.mapper;

import com.wanderers.hotelier_webservices.rest.model.Booking;
import com.wanderers.hotelier_webservices.server.dto.BookingDto;
import org.springframework.stereotype.Component;

/**
 * Class is responsible for mapping {@link BookingDto} DTO between REST and Server layers
 */
@Component("booking_mapper")
public class BookingMapper {

    public BookingDto mapToBookingDto(Booking booking) {
        return new BookingDto(booking.getAccommodationId(), booking.getCustomerId());
    }

    public Booking mapToRestBooking(BookingDto bookingDto) {
        return new Booking()
                .accommodationId(bookingDto.getAccommodationId())
                .customerId(bookingDto.getCustomerId());
    }
}
