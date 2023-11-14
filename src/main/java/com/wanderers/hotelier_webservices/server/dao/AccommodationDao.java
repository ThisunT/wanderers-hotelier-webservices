package com.wanderers.hotelier_webservices.server.dao;

import com.wanderers.hotelier_webservices.server.dto.AccommodationDto;
import com.wanderers.hotelier_webservices.server.exception.AccommodationDaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import static com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.INSERT_ACCOMMODATION;

/**
 * Class is responsible for datasource manipulations of accommodation
 */
@Repository("accommodation_dao")
public class AccommodationDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    AccommodationDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public AccommodationDto create(AccommodationDto accommodationDto) throws AccommodationDaoException {

        try {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("name", accommodationDto.getName())
                    .addValue("rating", accommodationDto.getRating())
                    .addValue("category", accommodationDto.getCategory())
                    .addValue("city", accommodationDto.getCity())
                    .addValue("state", accommodationDto.getState())
                    .addValue("country", accommodationDto.getCountry())
                    .addValue("zipCode", accommodationDto.getZipCode())
                    .addValue("address", accommodationDto.getAddress())
                    .addValue("image", accommodationDto.getImage())
                    .addValue("reputation", accommodationDto.getReputation())
                    .addValue("reputationBadge", accommodationDto.getReputationBadge().toString().toUpperCase())
                    .addValue("price", accommodationDto.getPrice())
                    .addValue("availability", accommodationDto.getAvailability())
                    .addValue("hotelierId", accommodationDto.getHotelierId());

            namedParameterJdbcTemplate.query(INSERT_ACCOMMODATION, params,  resultSet -> {
                accommodationDto.setId(Integer.parseInt(resultSet.getString("id")));
            });

            return accommodationDto;
        } catch (Exception e) {
            throw new AccommodationDaoException("Failed to create the accommodation record", e);
        }
    }

}
