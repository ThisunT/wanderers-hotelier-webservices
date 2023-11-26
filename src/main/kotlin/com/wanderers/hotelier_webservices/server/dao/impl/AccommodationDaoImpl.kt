package com.wanderers.hotelier_webservices.server.dao.impl

import com.wanderers.hotelier_webservices.mapper.AccommodationRowMapper
import com.wanderers.hotelier_webservices.rest.model.AccommodationPatchBody
import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum
import com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.*
import com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.DELETE_ACCOMMODATION_BY_ID
import com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.GET_ACCOMMODATION_BY_ID
import com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.GET_AVAILABILITY_BY_ACC_ID
import com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.GET_HOTELIER_BY_ACC_ID
import com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.INSERT_ACCOMMODATION
import com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.UPDATE_AVAILABILITY
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto
import com.wanderers.hotelier_webservices.server.exception.AccommodationDaoException
import com.wanderers.hotelier_webservices.server.exception.ResultNotFoundException
import lombok.SneakyThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Class is responsible for datasource manipulations of accommodation
 */
@Repository("accommodation_dao")
class AccommodationDaoImpl @Autowired internal constructor(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    @Throws(AccommodationDaoException::class)
    fun create(accommodationDto: AccommodationDto): AccommodationDto {
        return try {
            val params = MapSqlParameterSource()
                .addValue("name", accommodationDto.name)
                .addValue("rating", accommodationDto.rating)
                .addValue("category", accommodationDto.category)
                .addValue("city", accommodationDto.city)
                .addValue("state", accommodationDto.state)
                .addValue("country", accommodationDto.country)
                .addValue("zipCode", accommodationDto.zipCode)
                .addValue("address", accommodationDto.address)
                .addValue("image", accommodationDto.image)
                .addValue("reputation", accommodationDto.reputation)
                .addValue("reputationBadge", accommodationDto.reputationBadge!!.value.uppercase())
                .addValue("price", accommodationDto.price)
                .addValue("availability", accommodationDto.availability)
                .addValue("hotelierId", accommodationDto.hotelierId)
            namedParameterJdbcTemplate.query(INSERT_ACCOMMODATION, params) { resultSet ->
                accommodationDto.setId(
                    resultSet.getString("id").toInt()
                )
            }
            accommodationDto
        } catch (e: Exception) {
            throw AccommodationDaoException("Failed to create the accommodation record", e)
        }
    }

    @Throws(AccommodationDaoException::class)
    fun getAccommodations(
        hotelierId: String?, rating: Int?,
        city: String?, reputationBadge: ReputationBadgeEnum?
    ): List<AccommodationDto> {
        return try {
            val params = MapSqlParameterSource()
            params.addValue("hotelierId", hotelierId)
            val query =
                "SELECT * FROM accommodation acc INNER JOIN location loc ON acc.id = loc.accommodation_id where acc.hotelier_id = :hotelierId" +
                        Optional.ofNullable(rating).map { `val`: Int? ->
                            params.addValue("rating", `val`)
                            " AND rating = :rating"
                        }.orElse("") +
                        Optional.ofNullable(city).map { `val`: String? ->
                            params.addValue("city", `val`)
                            " AND city = :city"
                        }.orElse("") +
                        Optional.ofNullable(reputationBadge).map { rb: ReputationBadgeEnum ->
                            params.addValue("reputationBadge", rb.value.uppercase(Locale.getDefault()))
                            " AND reputation_badge = :reputationBadge::reputation_badge_enum"
                        }.orElse("")
            namedParameterJdbcTemplate.query(query, params, AccommodationRowMapper())
        } catch (e: Exception) {
            throw AccommodationDaoException("Failed to get accommodation records by criteria", e)
        }
    }

    @Throws(AccommodationDaoException::class, ResultNotFoundException::class)
    fun getAccommodation(id: Int): AccommodationDto {
        return try {
            val params = MapSqlParameterSource()
            params.addValue("id", id)
            namedParameterJdbcTemplate.queryForObject(GET_ACCOMMODATION_BY_ID, params, AccommodationRowMapper())
        } catch (e: EmptyResultDataAccessException) {
            throw ResultNotFoundException("An accommodation resource does not exist for id: $id", e)
        } catch (e: Exception) {
            throw AccommodationDaoException("Failed to get accommodation records by id", e)
        }
    }

    @SneakyThrows
    fun getHotelierById(id: Int): String {
        return try {
            val params = MapSqlParameterSource()
            params.addValue("id", id)
            namedParameterJdbcTemplate.queryForObject(GET_HOTELIER_BY_ACC_ID, params, String::class.java) as String
        } catch (e: EmptyResultDataAccessException) {
            throw ResultNotFoundException("An accommodation resource does not exist for id: $id", e)
        } catch (e: Exception) {
            throw AccommodationDaoException("Failed to get hotelier by accommodation id", e)
        }
    }

    @Transactional
    @Throws(AccommodationDaoException::class)
    fun patchAccommodation(id: Int, patchDTO: AccommodationPatchBody, reputationBadgeEnum: ReputationBadgeEnum) {
        try {
            val accParams = MapSqlParameterSource()
            val locParams = MapSqlParameterSource()
            val accQuery = buildAccQueryForPatch(accParams, id, patchDTO, reputationBadgeEnum)
            val locQuery = buildLocQueryForPatch(locParams, id, patchDTO)
            if (accQuery != null) {
                namedParameterJdbcTemplate.update(accQuery, accParams)
            }
            if (locQuery != null) {
                namedParameterJdbcTemplate.update(locQuery, locParams)
            }
        } catch (e: Exception) {
            throw AccommodationDaoException("Failed to patch accommodation by id", e)
        }
    }

    @Throws(AccommodationDaoException::class)
    fun deleteAccommodation(id: Int) {
        try {
            val params = MapSqlParameterSource()
            params.addValue("id", id)
            namedParameterJdbcTemplate.update(DELETE_ACCOMMODATION_BY_ID, params)
        } catch (e: Exception) {
            throw AccommodationDaoException("Failed to delete accommodation by id", e)
        }
    }

    @Throws(AccommodationDaoException::class, ResultNotFoundException::class)
    fun getAvailabilityByAccommodation(id: Int): Int {
        return try {
            val params = MapSqlParameterSource()
            params.addValue("id", id)
            namedParameterJdbcTemplate.queryForObject(GET_AVAILABILITY_BY_ACC_ID, params, Int::class.java) as Int
        } catch (e: EmptyResultDataAccessException) {
            throw ResultNotFoundException("An accommodation resource does not exist for id: $id", e)
        } catch (e: Exception) {
            throw AccommodationDaoException("Failed to get hotelier by accommodation id", e)
        }
    }

    @Throws(AccommodationDaoException::class)
    fun setAvailabilityByAccommodation(id: Int, newAvailability: Int) {
        try {
            val params = MapSqlParameterSource()
            params.addValue("id", id)
            params.addValue("newAvailability", newAvailability)
            namedParameterJdbcTemplate.update(UPDATE_AVAILABILITY, params)
        } catch (e: Exception) {
            throw AccommodationDaoException("Failed to get hotelier by accommodation id", e)
        }
    }

    private fun buildAccQueryForPatch(
        accParams: MapSqlParameterSource,
        id: Int,
        patchDTO: AccommodationPatchBody,
        reputationBadgeEnum: ReputationBadgeEnum
    ): String? {
        val accQueryBuilder = StringBuilder()
        val initUpdateAccQuery = "UPDATE accommodation SET "
        accQueryBuilder.append(initUpdateAccQuery)
            .append(Optional.ofNullable(patchDTO.name).map { `val`: String? ->
                accParams.addValue("name", `val`)
                " name = :name,"
            }.orElse(""))
            .append(Optional.ofNullable(patchDTO.rating).map { `val`: Int? ->
                accParams.addValue("rating", `val`)
                " rating = :rating,"
            }.orElse(""))
            .append(Optional.ofNullable(patchDTO.category).map { `val`: AccommodationPatchBody.Category? ->
                accParams.addValue("category", `val`)
                " category = :category::accommodation_category_enum,"
            }.orElse(""))
            .append(Optional.ofNullable(patchDTO.image).map { `val`: String? ->
                accParams.addValue("image", `val`)
                " image = :image,"
            }.orElse(""))
            .append(Optional.ofNullable(patchDTO.reputation).map { `val`: Int? ->
                accParams.addValue("reputation", `val`)
                " reputation = :reputation,"
            }.orElse(""))
            .append(Optional.ofNullable(reputationBadgeEnum).map { `val`: ReputationBadgeEnum ->
                accParams.addValue("reputationBadge", `val`.value.uppercase(Locale.getDefault()))
                " reputation_badge = :reputationBadge::reputation_badge_enum,"
            }.orElse(""))
            .append(Optional.ofNullable(patchDTO.price).map { `val`: Int? ->
                accParams.addValue("price", `val`)
                " price = :price,"
            }.orElse(""))
            .append(Optional.ofNullable(patchDTO.availability).map { `val`: Int? ->
                accParams.addValue("availability", `val`)
                " availability = :availability,"
            }.orElse(""))
        if (initUpdateAccQuery == accQueryBuilder.toString()) {
            return null
        }
        accQueryBuilder.deleteCharAt(accQueryBuilder.length - 1)
        accParams.addValue("id", id)
        accQueryBuilder.append(" WHERE id = :id")
        return accQueryBuilder.toString()
    }

    private fun buildLocQueryForPatch(
        locParams: MapSqlParameterSource,
        id: Int,
        patchDTO: AccommodationPatchBody
    ): String? {
        return if (patchDTO.location != null) {
            val locQueryBuilder = StringBuilder()
            val location = patchDTO.location
            val initUpdateLocQuery = "UPDATE location SET "
            locQueryBuilder.append(initUpdateLocQuery)
                .append(Optional.ofNullable(location.city).map { `val`: String? ->
                    locParams.addValue("city", `val`)
                    " city = :city,"
                }.orElse(""))
                .append(Optional.ofNullable(location.state).map { `val`: String? ->
                    locParams.addValue("state", `val`)
                    " state = :state,"
                }.orElse(""))
                .append(Optional.ofNullable(location.country).map { `val`: String? ->
                    locParams.addValue("country", `val`)
                    " country = :country,"
                }.orElse(""))
                .append(Optional.ofNullable(location.zipCode).map { `val`: String? ->
                    locParams.addValue("zipCode", `val`)
                    " zip_code = :zipCode,"
                }.orElse(""))
                .append(Optional.ofNullable(location.address).map { `val`: String? ->
                    locParams.addValue("address", `val`)
                    " address = :address,"
                }.orElse(""))
            if (initUpdateLocQuery == locQueryBuilder.toString()) {
                return null
            }
            locQueryBuilder.deleteCharAt(locQueryBuilder.length - 1)
            locParams.addValue("id", id)
            locQueryBuilder.append(" WHERE accommodation_id = :id")
            locQueryBuilder.toString()
        } else {
            null
        }
    }
}