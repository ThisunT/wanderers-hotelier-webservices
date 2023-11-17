package com.wanderers.hotelier_webservices.server.service;

import com.wanderers.hotelier_webservices.rest.model.Booking;
import com.wanderers.hotelier_webservices.server.dao.AccommodationDao;
import com.wanderers.hotelier_webservices.server.dao.BookingDao;
import com.wanderers.hotelier_webservices.server.exception.BookingUnavailableException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class is responsible for maintaining business logic of bookings
 */
@Service("booking_service")
public class BookingService {

    private final AccommodationDao accommodationDao;
    private final BookingDao bookingDao;

    @Autowired
    BookingService(AccommodationDao accommodationDao, BookingDao bookingDao) {
        this.accommodationDao = accommodationDao;
        this.bookingDao = bookingDao;
    }

    @SneakyThrows
    @Transactional
    public Booking create(Booking booking) {
        int availability = accommodationDao.getAvailabilityByAccommodation(booking.getAccommodationId());

        if (availability > 0) {
            accommodationDao.setAvailabilityByAccommodation(booking.getAccommodationId(), availability - 1);
            return bookingDao.create(booking);
        } else {
            throw new BookingUnavailableException("Bookings are unavailable for accommodation: " + booking.getAccommodationId());
        }
    }
}
