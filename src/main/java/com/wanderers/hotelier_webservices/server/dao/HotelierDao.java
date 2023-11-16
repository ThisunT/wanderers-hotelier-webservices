package com.wanderers.hotelier_webservices.server.dao;

import com.wanderers.hotelier_webservices.server.exception.AccommodationDaoException;
import com.wanderers.hotelier_webservices.server.exception.HotelierDaoException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import static com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.EXISTS_HOTELIER;
import static com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.INSERT_ACCOMMODATION;

@Repository("hotelier_dao")
public class HotelierDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    HotelierDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @SneakyThrows
    public Boolean isExistingHotelier(String hotelierId) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("id", hotelierId);

            return namedParameterJdbcTemplate.queryForObject(EXISTS_HOTELIER, params, Boolean.class);
        } catch (Exception e) {
            throw new HotelierDaoException("Failed get the hotelier record", e);
        }
    }
}
