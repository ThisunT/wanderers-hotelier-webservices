package com.wanderers.hotelier_webservices.server.dao

import com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.EXISTS_CUSTOMER
import com.wanderers.hotelier_webservices.server.exception.HotelierDaoException
import lombok.SneakyThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/**
 * Class is responsible for datasource manipulations of customer
 */
@Repository("customer_dao")
class CustomerDao @Autowired internal constructor(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    @SneakyThrows
    fun isExistingCustomer(customerId: Int): Boolean {
        return try {
            val params = MapSqlParameterSource()
                .addValue("id", customerId)
            namedParameterJdbcTemplate.queryForObject<Boolean>(EXISTS_CUSTOMER, params, Boolean::class)
        } catch (e: Exception) {
            throw HotelierDaoException("Failed get the customer record", e)
        }
    }
}