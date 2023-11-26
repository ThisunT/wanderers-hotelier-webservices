package com.wanderers.hotelier_webservices.server.service.api

import com.wanderers.hotelier_webservices.server.dto.BookingDto
import com.wanderers.hotelier_webservices.server.exception.BookingServiceException
import com.wanderers.hotelier_webservices.server.exception.BookingUnavailableException
import com.wanderers.hotelier_webservices.server.exception.ResultNotFoundException

/**
 * Contract that is responsible for maintaining business logic of bookings
 */
interface BookingService {
    /**
     * Create a booking over the booking data
     *
     * @param booking
     * @return [BookingDto]
     */
    @kotlin.Throws(BookingServiceException::class, BookingUnavailableException::class, ResultNotFoundException::class)
    fun create(booking: BookingDto?): BookingDto?
}