package com.wanderers.hotelier_webservices.server.service.impl;

import com.wanderers.hotelier_webservices.server.dao.api.AccommodationDao;
import com.wanderers.hotelier_webservices.server.dao.api.BookingDao;
import com.wanderers.hotelier_webservices.server.dto.BookingDto;
import com.wanderers.hotelier_webservices.server.exception.BookingServiceException;
import com.wanderers.hotelier_webservices.server.exception.BookingUnavailableException;
import com.wanderers.hotelier_webservices.server.exception.ResultNotFoundException;
import com.wanderers.hotelier_webservices.server.service.api.BookingService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class is responsible for maintaining business logic of bookings
 */
@Service("booking_service")
public class BookingServiceImpl implements BookingService {

    private final AccommodationDao accommodationDao;
    private final BookingDao bookingDao;

    @Autowired
    BookingServiceImpl(AccommodationDao accommodationDao, BookingDao bookingDao) {
        this.accommodationDao = accommodationDao;
        this.bookingDao = bookingDao;
    }

    @Override
    @SneakyThrows
    @Transactional
    public BookingDto create(BookingDto booking) {
        try {
            int availability = accommodationDao.getAvailabilityByAccommodation(booking.getAccommodationId());

            if (availability > 0) {
                accommodationDao.setAvailabilityByAccommodation(booking.getAccommodationId(), availability - 1);
                return bookingDao.create(booking);
            } else {
                throw new BookingUnavailableException("Bookings are unavailable for accommodation: " + booking.getAccommodationId());
            }
        } catch (ResultNotFoundException | BookingUnavailableException e) {
            throw e;
        } catch (Exception e) {
            throw new BookingServiceException("Failed to create the booking", e);
        }

    }
}
