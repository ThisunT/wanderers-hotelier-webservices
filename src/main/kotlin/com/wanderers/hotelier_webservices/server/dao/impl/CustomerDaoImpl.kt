package com.wanderers.hotelier_webservices.server.dao.impl

import com.wanderers.hotelier_webservices.server.dao.api.CustomerDao
import com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.EXISTS_CUSTOMER
import com.wanderers.hotelier_webservices.server.exception.HotelierDaoException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/**
 * Class is responsible for datasource manipulations of customer
 */
@Repository("customer_dao")
class CustomerDaoImpl @Autowired internal constructor(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): CustomerDao {

    override fun isExistingCustomer(customerId: Int): Boolean {
        return try {
            val params = MapSqlParameterSource()
                .addValue("id", customerId)
            namedParameterJdbcTemplate.queryForObject(EXISTS_CUSTOMER, params, Boolean::class.java) == true
        } catch (e: Exception) {
            throw HotelierDaoException("Failed get the customer record", e)
        }
    }
}