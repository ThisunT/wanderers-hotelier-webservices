package com.wanderers.hotelier_webservices.rest.delegate

import com.wanderers.hotelier_webservices.rest.api.BookingApiDelegate
import com.wanderers.hotelier_webservices.rest.exception.ResourceNotFoundException
import com.wanderers.hotelier_webservices.rest.model.Booking
import com.wanderers.hotelier_webservices.rest.validate.BookingValidator
import com.wanderers.hotelier_webservices.server.service.BookingService
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
class BookingApiDelegateImpl @Autowired constructor(
    servletRequest: HttpServletRequest?,
    private val bookingValidator: BookingValidator,
    private val bookingService: BookingService
) :
    AbstractApiDelegate(servletRequest), BookingApiDelegate {

    fun submitBooking(booking: Booking?): ResponseEntity<Booking> {
        bookingValidator.validateBooking(booking, token)
        val createdBooking: Booking = bookingService.create(booking)
        return ResponseEntity(createdBooking, HttpStatus.CREATED)
    }

    private val token: String
        get() = Optional.ofNullable(getServletRequest().getHeader("Is-Valid-Token"))
            .orElseThrow<RuntimeException> { ResourceNotFoundException("Is-Valid-Token header cannot be null") }
}
