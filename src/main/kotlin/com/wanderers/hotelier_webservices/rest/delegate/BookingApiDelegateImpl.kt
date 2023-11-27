package com.wanderers.hotelier_webservices.rest.delegate

import com.wanderers.hotelier_webservices.rest.api.BookingApiDelegate
import com.wanderers.hotelier_webservices.rest.mapper.BookingMapper
import com.wanderers.hotelier_webservices.rest.model.Booking
import com.wanderers.hotelier_webservices.rest.validate.BookingValidator
import com.wanderers.hotelier_webservices.server.dto.BookingDto
import com.wanderers.hotelier_webservices.server.service.api.BookingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

/**
 * Class implements the endpoints of booking resource.
 */
@Service("booking_api_delegate")
class BookingApiDelegateImpl @Autowired internal constructor(private val bookingValidator: BookingValidator,
                                                             bookingService: BookingService, bookingMapper: BookingMapper
) : BookingApiDelegate {
    private val bookingService: BookingService
    private val bookingMapper: BookingMapper

    init {
        this.bookingService = bookingService
        this.bookingMapper = bookingMapper
    }

    override fun submitBooking(booking: Booking, isValidToken: Boolean): ResponseEntity<Booking> {
        bookingValidator.validateBooking(booking, isValidToken)

        val bookingDto = bookingMapper.mapToBookingDto(booking)
        val createdBooking: BookingDto = bookingService.create(bookingDto)
        val bookingResponse = bookingMapper.mapToRestBooking(createdBooking)

        return ResponseEntity(bookingResponse, HttpStatus.CREATED)
    }
}