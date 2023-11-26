package com.wanderers.hotelier_webservices.server.dao.impl

import com.wanderers.hotelier_webservices.rest.model.Booking
import com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.INSERT_BOOKING
import com.wanderers.hotelier_webservices.server.exception.AccommodationDaoException
import com.wanderers.hotelier_webservices.server.exception.BookingDaoException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/**
 * Class is responsible for datasource manipulations of booking
 */
@Repository("booking_dao")
class BookingDaoImpl @Autowired internal constructor(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    @Throws(AccommodationDaoException::class, BookingDaoException::class)
    fun create(booking: Booking?): Booking {
        return try {
            val params = MapSqlParameterSource()
                .addValue("accommodationId", booking.accommodationId)
                .addValue("customerId", booking.customerId)
            namedParameterJdbcTemplate.update(INSERT_BOOKING, params)
            booking
        } catch (e: Exception) {
            throw BookingDaoException("Failed to create the booking record", e)
        }
    }
}