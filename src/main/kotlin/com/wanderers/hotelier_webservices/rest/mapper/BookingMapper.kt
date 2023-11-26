package com.wanderers.hotelier_webservices.rest.mapper

import com.wanderers.hotelier_webservices.rest.model.Booking
import com.wanderers.hotelier_webservices.server.dto.BookingDto
import org.springframework.stereotype.Component

/**
 * Class is responsible for mapping [BookingDto] DTO between REST and Server layers
 */
@Component("booking_mapper")
class BookingMapper {
    fun mapToBookingDto(booking: Booking): BookingDto {
        return BookingDto(booking.accommodationId, booking.customerId)
    }

    fun mapToRestBooking(bookingDto: BookingDto): Booking {
        return Booking()
            .accommodationId(bookingDto.getAccommodationId())
            .customerId(bookingDto.getCustomerId())
    }
}