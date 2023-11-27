package com.wanderers.hotelier_webservices.server.mapper

import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

/**
 * Class is responsible for mapping a database accommodation entity into a server object
 */
class AccommodationRowMapper : RowMapper<AccommodationDto> {
    override fun mapRow(rs: ResultSet, rowNum: Int): AccommodationDto {

        return AccommodationDto(
            id = rs.getInt("id"),
            hotelierId = rs.getString("hotelier_id"),
            name = rs.getString("name"),
            rating = rs.getInt("rating"),
            category = rs.getString("category"),
            city = rs.getString("city"),
            state = rs.getString("state"),
            country = rs.getString("country"),
            zipCode = rs.getString("zip_code"),
            address = rs.getString("address"),
            image = rs.getString("image"),
            reputation = rs.getInt("reputation"),
            reputationBadge = ReputationBadgeEnum.valueOf(rs.getString("reputation_badge").lowercase()),
            price = rs.getInt("price"),
            availability = rs.getInt("availability")
        )
    }
}
