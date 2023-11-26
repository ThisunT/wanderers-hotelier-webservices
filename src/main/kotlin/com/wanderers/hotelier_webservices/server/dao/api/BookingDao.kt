package com.wanderers.hotelier_webservices.server.dao.api

import com.wanderers.hotelier_webservices.server.dto.BookingDto
import com.wanderers.hotelier_webservices.server.exception.BookingDaoException

/**
 * Data access contract for the booking entity
 */
interface BookingDao {
    /**
     * Create a booking record over the provided data
     *
     * @param booking
     * @return [BookingDto]
     * @throws BookingDaoException
     */
    @kotlin.Throws(BookingDaoException::class)
    fun create(booking: BookingDto?): BookingDto?
}