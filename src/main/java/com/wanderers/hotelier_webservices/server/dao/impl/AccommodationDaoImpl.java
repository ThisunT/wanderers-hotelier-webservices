package com.wanderers.hotelier_webservices.server.dao.impl;

import com.wanderers.hotelier_webservices.rest.model.AccommodationPatchBody;
import com.wanderers.hotelier_webservices.rest.model.OptionalLocation;
import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum;
import com.wanderers.hotelier_webservices.server.dao.api.AccommodationDao;
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto;
import com.wanderers.hotelier_webservices.server.exception.AccommodationDaoException;
import com.wanderers.hotelier_webservices.server.exception.ResultNotFoundException;
import com.wanderers.hotelier_webservices.server.mapper.AccommodationRowMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.*;

/**
 * Class is responsible for datasource manipulations of accommodation
 */
@Log4j2
@Repository("accommodation_dao")
public class AccommodationDaoImpl implements AccommodationDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    AccommodationDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public AccommodationDto create(AccommodationDto accommodationDto) throws AccommodationDaoException {
        log.info("Creating an accommodation for: {}", accommodationDto.getHotelierId());
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
            log.error("An error occurred while creating an accommodation for: {}", accommodationDto.getHotelierId(), e);
            throw new AccommodationDaoException("Failed to create the accommodation record", e);
        }
    }

    @Override
    public List<AccommodationDto> getAccommodations(String hotelierId, Integer rating,
                                                    String city, ReputationBadgeEnum reputationBadge) throws AccommodationDaoException {
        log.info("Fetching accommodations for: {}", hotelierId);
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("hotelierId", hotelierId);

            String query = "SELECT * FROM accommodation acc INNER JOIN location loc ON acc.id = loc.accommodation_id where acc.hotelier_id = :hotelierId" +
                    Optional.ofNullable(rating).map(val -> {
                        params.addValue("rating", val);
                        return " AND rating = :rating";
                    }).orElse("") +
                    Optional.ofNullable(city).map(val -> {
                        params.addValue("city", val);
                        return " AND city = :city";
                    }).orElse("") +
                    Optional.ofNullable(reputationBadge).map(rb -> {
                        params.addValue("reputationBadge", rb.getValue().toUpperCase());
                        return " AND reputation_badge = :reputationBadge::reputation_badge_enum";
                    }).orElse("");

            return namedParameterJdbcTemplate.query(query, params, new AccommodationRowMapper());
        } catch (Exception e) {
            log.error("An error occurred while fetching an accommodations", e);
            throw new AccommodationDaoException("Failed to get accommodation records by criteria", e);
        }

    }

    @Override
    public AccommodationDto getAccommodation(int id) throws AccommodationDaoException, ResultNotFoundException {
        log.info("Fetching accommodations of Id: {}", id);
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", id);

            return namedParameterJdbcTemplate.queryForObject(GET_ACCOMMODATION_BY_ID, params, new AccommodationRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new ResultNotFoundException("An accommodation resource does not exist for id: " + id, e);
        } catch (Exception e) {
            log.error("An error occurred while fetching an accommodations by id", e);
            throw new AccommodationDaoException("Failed to get accommodation records by id", e);
        }
    }

    @Override
    public String getHotelierByAccommodationId(int id) throws ResultNotFoundException, AccommodationDaoException {
        log.info("Fetching hotelier of Id: {}", id);
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", id);

            return namedParameterJdbcTemplate.queryForObject(GET_HOTELIER_BY_ACC_ID, params, String.class);
        } catch (EmptyResultDataAccessException e) {
            throw new ResultNotFoundException("An accommodation resource does not exist for id: " + id, e);
        } catch (Exception e) {
            log.error("An error occurred while fetching an accommodations by id", e);
            throw new AccommodationDaoException("Failed to get hotelier by accommodation id", e);
        }
    }

    @Override
    @Transactional
    public void patchAccommodation(int id, AccommodationPatchBody patchDTO, ReputationBadgeEnum reputationBadgeEnum) throws AccommodationDaoException {
        log.info("Patching accommodation of Id: {}", id);
        try {
            MapSqlParameterSource accParams = new MapSqlParameterSource();
            MapSqlParameterSource locParams = new MapSqlParameterSource();

            String accQuery = buildAccQueryForPatch(accParams, id, patchDTO, reputationBadgeEnum);
            String locQuery = buildLocQueryForPatch(locParams, id, patchDTO);

            if (accQuery != null) {
                namedParameterJdbcTemplate.update(accQuery, accParams);
            }
            if (locQuery != null) {
                namedParameterJdbcTemplate.update(locQuery, locParams);
            }
        } catch (Exception e) {
            log.error("An error occurred while patching accommodation {}", id,  e);
            throw new AccommodationDaoException("Failed to patch accommodation by id", e);
        }
    }

    @Override
    public void deleteAccommodation(int id) throws AccommodationDaoException {
        log.info("Deleting accommodation of Id: {}", id);
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", id);

            namedParameterJdbcTemplate.update(DELETE_ACCOMMODATION_BY_ID, params);
        } catch (Exception e) {
            log.error("An error occurred while deleting accommodation {}", id,  e);
            throw new AccommodationDaoException("Failed to delete accommodation by id", e);
        }
    }

    @Override
    public Integer getAvailabilityByAccommodation(int id) throws AccommodationDaoException, ResultNotFoundException {
        log.info("Fetching availability of Id: {}", id);
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", id);

            return namedParameterJdbcTemplate.queryForObject(GET_AVAILABILITY_BY_ACC_ID, params, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            throw new ResultNotFoundException("An accommodation resource does not exist for id: " + id, e);
        } catch (Exception e) {
            log.error("An error occurred while getting availability of accommodation {}", id,  e);
            throw new AccommodationDaoException("Failed to get hotelier by accommodation id", e);
        }
    }

    @Override
    public void setAvailabilityByAccommodation(int id, int newAvailability) throws AccommodationDaoException {
        log.info("Setting availability of accommodation Id: {}", id);
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", id);
            params.addValue("newAvailability", newAvailability);

            namedParameterJdbcTemplate.update(UPDATE_AVAILABILITY, params);
        } catch (Exception e) {
            log.error("An error occurred while setting availability of accommodation {}", id,  e);
            throw new AccommodationDaoException("Failed to get hotelier by accommodation id", e);
        }
    }

    private String buildAccQueryForPatch(MapSqlParameterSource accParams, int id, AccommodationPatchBody patchDTO, ReputationBadgeEnum reputationBadgeEnum) {
        StringBuilder accQueryBuilder = new StringBuilder();
        final String initUpdateAccQuery = "UPDATE accommodation SET ";

        accQueryBuilder.append(initUpdateAccQuery)
                .append(Optional.ofNullable(patchDTO.getName()).map(val -> {
                    accParams.addValue("name", val);
                    return " name = :name,";
                }).orElse(""))
                .append(Optional.ofNullable(patchDTO.getRating()).map(val -> {
                    accParams.addValue("rating", val);
                    return " rating = :rating,";
                }).orElse(""))
                .append(Optional.ofNullable(patchDTO.getCategory()).map(val -> {
                    accParams.addValue("category", val);
                    return " category = :category::accommodation_category_enum,";
                }).orElse(""))
                .append(Optional.ofNullable(patchDTO.getImage()).map(val -> {
                    accParams.addValue("image", val);
                    return " image = :image,";
                }).orElse(""))
                .append(Optional.ofNullable(patchDTO.getReputation()).map(val -> {
                    accParams.addValue("reputation", val);
                    return " reputation = :reputation,";
                }).orElse(""))
                .append(Optional.ofNullable(reputationBadgeEnum).map(val -> {
                    accParams.addValue("reputationBadge", val.getValue().toUpperCase());
                    return " reputation_badge = :reputationBadge::reputation_badge_enum,";
                }).orElse(""))
                .append(Optional.ofNullable(patchDTO.getPrice()).map(val -> {
                    accParams.addValue("price", val);
                    return " price = :price,";
                }).orElse(""))
                .append(Optional.ofNullable(patchDTO.getAvailability()).map(val -> {
                    accParams.addValue("availability", val);
                    return " availability = :availability,";
                }).orElse(""));

        if (initUpdateAccQuery.equals(accQueryBuilder.toString())) {
            return null;
        }

        accQueryBuilder.deleteCharAt(accQueryBuilder.length() - 1);

        accParams.addValue("id", id);
        accQueryBuilder.append(" WHERE id = :id");

        return accQueryBuilder.toString();
    }

    private String buildLocQueryForPatch(MapSqlParameterSource locParams, int id, AccommodationPatchBody patchDTO) {
        if (patchDTO.getLocation() != null) {
            StringBuilder locQueryBuilder = new StringBuilder();
            OptionalLocation location = patchDTO.getLocation();

            final String initUpdateLocQuery = "UPDATE location SET ";

            locQueryBuilder.append(initUpdateLocQuery)
                    .append(Optional.ofNullable(location.getCity()).map(val -> {
                        locParams.addValue("city", val);
                        return " city = :city,";
                    }).orElse(""))
                    .append(Optional.ofNullable(location.getState()).map(val -> {
                        locParams.addValue("state", val);
                        return " state = :state,";
                    }).orElse(""))
                    .append(Optional.ofNullable(location.getCountry()).map(val -> {
                        locParams.addValue("country", val);
                        return " country = :country,";
                    }).orElse(""))
                    .append(Optional.ofNullable(location.getZipCode()).map(val -> {
                        locParams.addValue("zipCode", val);
                        return " zip_code = :zipCode,";
                    }).orElse(""))
                    .append(Optional.ofNullable(location.getAddress()).map(val -> {
                        locParams.addValue("address", val);
                        return " address = :address,";
                    }).orElse(""));

            if (initUpdateLocQuery.equals(locQueryBuilder.toString())) {
                return null;
            }

            locQueryBuilder.deleteCharAt(locQueryBuilder.length() - 1);

            locParams.addValue("id", id);
            locQueryBuilder.append(" WHERE accommodation_id = :id");

            return locQueryBuilder.toString();
        } else {
            return null;
        }
    }
}
