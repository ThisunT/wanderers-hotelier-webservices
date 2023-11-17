package com.wanderers.hotelier_webservices.server.service

import com.wanderers.hotelier_webservices.rest.model.Booking
import com.wanderers.hotelier_webservices.server.dao.AccommodationDao
import com.wanderers.hotelier_webservices.server.dao.BookingDao
import lombok.SneakyThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("booking_service")
class BookingService @Autowired internal constructor(accommodationDao: AccommodationDao, bookingDao: BookingDao) {
    private val accommodationDao: AccommodationDao
    private val bookingDao: BookingDao

    init {
        this.accommodationDao = accommodationDao
        this.bookingDao = bookingDao
    }

    @SneakyThrows
    @Transactional
    fun create(booking: Booking?): Booking {
        val availability: Int = accommodationDao.getAvailabilityByAccommodation(booking.accommodationId)
        return if (availability > 0) {
            accommodationDao.setAvailabilityByAccommodation(booking.accommodationId, availability - 1)
            bookingDao.create(booking)
        } else {
            throw BookingUnavailableException("Bookings are unavailable for accommodation: " + booking.accommodationId)
        }
    }
}