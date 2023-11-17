package com.wanderers.hotelier_webservices.mapper

import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

class AccommodationRowMapper : RowMapper<AccommodationDto> {
    @kotlin.Throws(SQLException::class)
    override fun mapRow(rs: ResultSet, rowNum: Int): AccommodationDto {
        val accommodationRow = AccommodationDto()
        accommodationRow.setId(rs.getString("id").toInt())
        accommodationRow.setHotelierId(rs.getString("hotelier_id"))
        accommodationRow.setName(rs.getString("name"))
        accommodationRow.setRating(rs.getString("rating").toInt())
        accommodationRow.setCategory(rs.getString("category"))
        accommodationRow.setCity(rs.getString("city"))
        accommodationRow.setState(rs.getString("state"))
        accommodationRow.setCountry(rs.getString("country"))
        accommodationRow.setZipCode(rs.getString("zip_code"))
        accommodationRow.setAddress(rs.getString("address"))
        accommodationRow.setImage(rs.getString("image"))
        accommodationRow.setReputation(rs.getString("reputation").toInt())
        accommodationRow.setReputationBadge(
            ReputationBadgeEnum.fromValue(
                rs.getString("reputation_badge").lowercase(Locale.getDefault())
            )
        )
        accommodationRow.setPrice(rs.getString("price").toInt())
        accommodationRow.setAvailability(rs.getString("availability").toInt())
        return accommodationRow
    }
}