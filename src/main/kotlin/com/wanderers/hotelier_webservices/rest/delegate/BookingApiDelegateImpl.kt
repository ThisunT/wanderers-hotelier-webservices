package com.wanderers.hotelier_webservices.rest.delegate

import com.wanderers.hotelier_webservices.rest.api.BookingApiDelegate
import com.wanderers.hotelier_webservices.rest.exception.ResourceNotFoundException
import com.wanderers.hotelier_webservices.rest.mapper.BookingMapper
import com.wanderers.hotelier_webservices.rest.model.Booking
import com.wanderers.hotelier_webservices.rest.validate.BookingValidator
import com.wanderers.hotelier_webservices.server.dto.BookingDto
import com.wanderers.hotelier_webservices.server.service.api.BookingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * Class implements the endpoints of booking resource.
 */
@Service("booking_api_delegate")
class BookingApiDelegateImpl @Autowired internal constructor(
    servletRequest: HttpServletRequest?, private val bookingValidator: BookingValidator,
    bookingService: BookingService, bookingMapper: BookingMapper
) : AbstractApiDelegate(servletRequest!!), BookingApiDelegate {
    private val bookingService: BookingService
    private val bookingMapper: BookingMapper

    init {
        this.bookingService = bookingService
        this.bookingMapper = bookingMapper
    }

    fun submitBooking(booking: Booking?): ResponseEntity<Booking> {
        bookingValidator.validateBooking(booking, token)
        val bookingDto = bookingMapper.mapToBookingDto(booking!!)
        val createdBooking: BookingDto = bookingService.create(bookingDto)
        val bookingResponse = bookingMapper.mapToRestBooking(createdBooking)
        return ResponseEntity(bookingResponse, HttpStatus.CREATED)
    }

    private val token: String
        private get() = Optional.ofNullable(getServletRequest().getHeader("Is-Valid-Token"))
            .orElseThrow {
                ResourceNotFoundException(
                    "Is-Valid-Token header cannot be null"
                )
            }
}