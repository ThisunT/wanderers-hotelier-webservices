package com.wanderers.hotelier_webservices.server.dao.impl

import com.wanderers.hotelier_webservices.server.dao.api.BookingDao
import com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.INSERT_BOOKING
import com.wanderers.hotelier_webservices.server.dto.BookingDto
import com.wanderers.hotelier_webservices.server.exception.BookingDaoException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/**
 * Class is responsible for datasource manipulations of booking
 */
@Repository("booking_dao")
class BookingDaoImpl @Autowired internal constructor(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) :
    BookingDao {

    @Throws(BookingDaoException::class)
    override fun create(booking: BookingDto): BookingDto {
        try {
            val params = MapSqlParameterSource()
                .addValue("accommodationId", booking.accommodationId)
                .addValue("customerId", booking.customerId)
            namedParameterJdbcTemplate.update(INSERT_BOOKING, params)
            return booking
        } catch (e: Exception) {
            throw BookingDaoException("Failed to create the booking record", e)
        }
    }

}