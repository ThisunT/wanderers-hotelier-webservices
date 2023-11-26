package com.wanderers.hotelier_webservices.server.dao.impl;

import com.wanderers.hotelier_webservices.server.dao.api.HotelierDao;
import com.wanderers.hotelier_webservices.server.exception.HotelierDaoException;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import static com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.EXISTS_HOTELIER;

/**
 * Class is responsible for datasource manipulations of hotelier
 */
@Log4j2
@Repository("hotelier_dao")
public class HotelierDaoImpl implements HotelierDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    HotelierDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Boolean isExistingHotelier(String hotelierId) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("id", hotelierId);

            return namedParameterJdbcTemplate.queryForObject(EXISTS_HOTELIER, params, Boolean.class);
        } catch (Exception e) {
            log.error("An error occurred while fetching hotelier", e);
            throw new HotelierDaoException("Failed to get the hotelier record", e);
        }
    }
}
