package com.wanderers.hotelier_webservices.server.dao;

import com.wanderers.hotelier_webservices.rest.model.Booking;
import com.wanderers.hotelier_webservices.server.exception.AccommodationDaoException;
import com.wanderers.hotelier_webservices.server.exception.BookingDaoException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import static com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.INSERT_BOOKING;

/**
 * Class is responsible for datasource manipulations of booking
 */
@Log4j2
@Repository("booking_dao")
public class BookingDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    BookingDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Booking create(Booking booking) throws AccommodationDaoException, BookingDaoException {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("accommodationId", booking.getAccommodationId())
                    .addValue("customerId", booking.getCustomerId());

            namedParameterJdbcTemplate.update(INSERT_BOOKING, params);

            return booking;
        } catch (Exception e) {
            log.error("An error occurred while creating the booking", e);
            throw new BookingDaoException("Failed to create the booking record", e);
        }
    }
}
