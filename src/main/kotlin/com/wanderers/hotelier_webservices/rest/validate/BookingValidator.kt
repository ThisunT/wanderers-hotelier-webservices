package com.wanderers.hotelier_webservices.rest.validate

import com.wanderers.hotelier_webservices.rest.model.Booking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.lang.Boolean
import kotlin.Int
import kotlin.String

@Component("booking_validator")
class BookingValidator @Autowired internal constructor(customerDao: CustomerDao) {
    private val customerDao: CustomerDao

    init {
        this.customerDao = customerDao
    }

    fun validateBooking(booking: Booking?, tokenStatus: String) {
        validateToken(tokenStatus)
        validateCustomer(booking.customerId!!)
    }

    private fun validateToken(tokenStatus: String) {
        if (!Boolean.parseBoolean(tokenStatus)) {
            throw ExpiredTokenException("Token is expired")
        }
    }

    private fun validateCustomer(customerId: Int) {
        if (Boolean.FALSE == customerDao.isExistingCustomer(customerId)) {
            throw ResourceNotFoundException("$customerId does not exists in the system. Please register!")
        }
    }
}