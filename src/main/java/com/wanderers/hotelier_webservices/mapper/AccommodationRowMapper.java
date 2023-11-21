package com.wanderers.hotelier_webservices.mapper;

import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum;
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class is responsible for mapping a database accommodation entity into a server object
 */
public class AccommodationRowMapper implements RowMapper<AccommodationDto> {

    @Override
    public AccommodationDto mapRow(ResultSet rs, int rowNum) throws SQLException {

        AccommodationDto accommodationRow = new AccommodationDto();

        accommodationRow.setId(Integer.parseInt(rs.getString("id")));
        accommodationRow.setHotelierId(rs.getString("hotelier_id"));
        accommodationRow.setName(rs.getString("name"));
        accommodationRow.setRating(Integer.parseInt(rs.getString("rating")));
        accommodationRow.setCategory(rs.getString("category"));
        accommodationRow.setCity(rs.getString("city"));
        accommodationRow.setState(rs.getString("state"));
        accommodationRow.setCountry(rs.getString("country"));
        accommodationRow.setZipCode(rs.getString("zip_code"));
        accommodationRow.setAddress(rs.getString("address"));
        accommodationRow.setImage(rs.getString("image"));
        accommodationRow.setReputation(Integer.parseInt(rs.getString("reputation")));
        accommodationRow.setReputationBadge(ReputationBadgeEnum.fromValue(rs.getString("reputation_badge").toLowerCase()));
        accommodationRow.setPrice(Integer.parseInt(rs.getString("price")));
        accommodationRow.setAvailability(Integer.parseInt(rs.getString("availability")));

        return accommodationRow;
    }
}
