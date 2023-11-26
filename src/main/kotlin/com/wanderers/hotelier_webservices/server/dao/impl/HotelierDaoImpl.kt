package com.wanderers.hotelier_webservices.server.dao.impl

import com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.EXISTS_HOTELIER
import com.wanderers.hotelier_webservices.server.exception.HotelierDaoException
import lombok.SneakyThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/**
 * Class is responsible for datasource manipulations of hotelier
 */
@Repository("hotelier_dao")
class HotelierDaoImpl @Autowired internal constructor(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    @SneakyThrows
    fun isExistingHotelier(hotelierId: String?): Boolean {
        return try {
            val params = MapSqlParameterSource()
                .addValue("id", hotelierId)
            namedParameterJdbcTemplate.queryForObject<Boolean>(EXISTS_HOTELIER, params, Boolean::class.java)
        } catch (e: Exception) {
            throw HotelierDaoException("Failed get the hotelier record", e)
        }
    }
}