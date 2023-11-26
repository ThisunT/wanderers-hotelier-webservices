package com.wanderers.hotelier_webservices.server.service.impl

import com.wanderers.hotelier_webservices.server.dao.api.AccommodationDao
import com.wanderers.hotelier_webservices.server.dao.api.BookingDao
import com.wanderers.hotelier_webservices.server.dto.BookingDto
import com.wanderers.hotelier_webservices.server.exception.BookingServiceException
import com.wanderers.hotelier_webservices.server.exception.BookingUnavailableException
import com.wanderers.hotelier_webservices.server.exception.ResultNotFoundException
import com.wanderers.hotelier_webservices.server.service.api.BookingService
import lombok.SneakyThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Class is responsible for maintaining business logic of bookings
 */
@Service("booking_service")
class BookingServiceImpl @Autowired internal constructor(accommodationDao: AccommodationDao, bookingDao: BookingDao) :
    BookingService {
    private val accommodationDao: AccommodationDao
    private val bookingDao: BookingDao

    init {
        this.accommodationDao = accommodationDao
        this.bookingDao = bookingDao
    }

    @SneakyThrows
    @Transactional
    override fun create(booking: BookingDto?): BookingDto? {
        return try {
            val availability: Int = accommodationDao.getAvailabilityByAccommodation(booking.getAccommodationId())
            if (availability > 0) {
                accommodationDao.setAvailabilityByAccommodation(booking.getAccommodationId(), availability - 1)
                bookingDao.create(booking)
            } else {
                throw BookingUnavailableException("Bookings are unavailable for accommodation: " + booking.getAccommodationId())
            }
        } catch (e: ResultNotFoundException) {
            throw e
        } catch (e: BookingUnavailableException) {
            throw e
        } catch (e: Exception) {
            throw BookingServiceException("Failed to create the booking", e)
        }
    }
}