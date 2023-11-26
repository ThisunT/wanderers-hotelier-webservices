package com.wanderers.hotelier_webservices.server.dao.api;

import com.wanderers.hotelier_webservices.server.dto.BookingDto;
import com.wanderers.hotelier_webservices.server.exception.BookingDaoException;

/**
 * Data access contract for the booking entity
 */
public interface BookingDao {

    /**
     * Create a booking record over the provided data
     *
     * @param booking
     * @return {@link BookingDto}
     * @throws BookingDaoException
     */
    BookingDto create(BookingDto booking) throws BookingDaoException;
}
