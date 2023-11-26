package com.wanderers.hotelier_webservices.server.mapper

import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

/**
 * Class is responsible for mapping a database accommodation entity into a server object
 */
class AccommodationRowMapper : RowMapper<AccommodationDto> {
    @kotlin.Throws(SQLException::class)
    override fun mapRow(rs: ResultSet, rowNum: Int): AccommodationDto {
        val accommodationRow = AccommodationDto()
        accommodationRow.id = rs.getString("id").toInt()
        accommodationRow.hotelierId = rs.getString("hotelier_id")
        accommodationRow.name = rs.getString("name")
        accommodationRow.rating = rs.getString("rating").toInt()
        accommodationRow.category = rs.getString("category")
        accommodationRow.city = rs.getString("city")
        accommodationRow.state = rs.getString("state")
        accommodationRow.country = rs.getString("country")
        accommodationRow.zipCode = rs.getString("zip_code")
        accommodationRow.address = rs.getString("address")
        accommodationRow.image = rs.getString("image")
        accommodationRow.reputation = rs.getString("reputation").toInt()
        accommodationRow.reputationBadge = ReputationBadgeEnum.fromValue(
            rs.getString("reputation_badge").lowercase(
                Locale.getDefault()
            )
        )
        accommodationRow.price = rs.getString("price").toInt()
        accommodationRow.availability = rs.getString("availability").toInt()
        return accommodationRow
    }
}