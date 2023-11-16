package com.wanderers.hotelier_webservices.server.dao;

import com.wanderers.hotelier_webservices.mapper.AccommodationRowMapper;
import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum;
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto;
import com.wanderers.hotelier_webservices.server.exception.AccommodationDaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
                    .addValue("reputationBadge", accommodationDto.getReputationBadge().getValue().toUpperCase())
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

    public List<AccommodationDto> getAccommodations(String hotelierId, Integer rating,
                                                    String city, ReputationBadgeEnum reputationBadge) throws AccommodationDaoException {
        try {
            String query = "SELECT * FROM accommodation acc INNER JOIN location loc ON acc.id = loc.accommodation_id where acc.hotelier_id = :hotelierId" +
                    Optional.ofNullable(rating).map(val -> " AND rating = :rating").orElse("") +
                    Optional.ofNullable(city).map(val -> " AND city = :city").orElse("") +
                    Optional.ofNullable(reputationBadge).map(rb -> " AND reputation_badge = :reputationBadge::reputation_badge_enum").orElse("");

            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("hotelierId", hotelierId);

            Optional.ofNullable(rating).ifPresent(val -> params.addValue("rating", val));
            Optional.ofNullable(reputationBadge).ifPresent(val -> params.addValue("reputationBadge", val.getValue().toUpperCase()));
            Optional.ofNullable(city).ifPresent(val -> params.addValue("city", city));

            return namedParameterJdbcTemplate.query(query, params, new AccommodationRowMapper());
        } catch (Exception e) {
            throw new AccommodationDaoException("Failed to get accommodation records by criteria", e);
        }

    }
}
