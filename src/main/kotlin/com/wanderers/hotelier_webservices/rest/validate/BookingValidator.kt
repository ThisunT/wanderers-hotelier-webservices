package com.wanderers.hotelier_webservices.rest.validate

import com.wanderers.hotelier_webservices.rest.exception.ExpiredTokenException
import com.wanderers.hotelier_webservices.rest.exception.ResourceNotFoundException
import com.wanderers.hotelier_webservices.rest.model.Booking
import com.wanderers.hotelier_webservices.server.service.api.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component("booking_validator")
class BookingValidator @Autowired internal constructor(private val customerService: CustomerService) {
    fun validateBooking(booking: Booking, tokenStatus: Boolean) {
        validateToken(tokenStatus)
        validateCustomer(booking.customerId!!)
    }

    private fun validateToken(tokenStatus: Boolean) {
        if (!tokenStatus) {
            throw ExpiredTokenException("Auth token is expired")
        }
    }

    private fun validateCustomer(customerId: Int) {
        if (!customerService.isExistingCustomer(customerId)) {
            throw ResourceNotFoundException("Customer: $customerId does not exists in the system. Please register!")
        }
    }
}