package com.wanderers.hotelier_webservices.server.dao.api

import com.wanderers.hotelier_webservices.rest.model.AccommodationPatchBody
import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto
import com.wanderers.hotelier_webservices.server.exception.AccommodationDaoException
import com.wanderers.hotelier_webservices.server.exception.ResultNotFoundException

/**
 * Data access contract for the accommodation entity
 */
interface AccommodationDao {
    /**
     * Creates an accommodation entity and a location entity
     *
     * @param accommodationDto
     * @return [AccommodationDto]
     * @throws AccommodationDaoException
     */
    @kotlin.Throws(AccommodationDaoException::class)
    fun create(accommodationDto: AccommodationDto): AccommodationDto

    /**
     * Get accommodation entities by a given criteria
     *
     * @param hotelierId
     * @param rating
     * @param city
     * @param reputationBadge
     * @return a list of [AccommodationDto]
     * @throws AccommodationDaoException
     */
    @kotlin.Throws(AccommodationDaoException::class)
    fun getAccommodations(
        hotelierId: String, rating: Int?,
        city: String?, reputationBadge: ReputationBadgeEnum?
    ): List<AccommodationDto>

    /**
     * Get an accommodation by id
     *
     * @param id
     * @return [AccommodationDto]
     * @throws AccommodationDaoException
     * @throws ResultNotFoundException
     */
    @kotlin.Throws(AccommodationDaoException::class, ResultNotFoundException::class)
    fun getAccommodation(id: Int): AccommodationDto?

    /**
     * Get a hotelier id given an accommodation id
     *
     * @param id
     * @return Hotelier Id
     */
    @kotlin.Throws(AccommodationDaoException::class, ResultNotFoundException::class)
    fun getHotelierByAccommodationId(id: Int): String

    /**
     * Update accommodation by provided new field values
     *
     * @param id
     * @param patchDTO
     * @param reputationBadgeEnum
     * @throws AccommodationDaoException
     */
    @kotlin.Throws(AccommodationDaoException::class)
    fun patchAccommodation(id: Int, patchDTO: AccommodationPatchBody, reputationBadgeEnum: ReputationBadgeEnum?)

    /**
     * Delete an accommodation
     * @param id
     */
    fun deleteAccommodation(id: Int)

    /**
     * Get the availability of an accommodation
     *
     * @param id
     * @return
     * @throws AccommodationDaoException
     * @throws ResultNotFoundException
     */
    @kotlin.Throws(AccommodationDaoException::class, ResultNotFoundException::class)
    fun getAvailabilityByAccommodation(id: Int): Int

    /**
     * Set the updated availability of an accommodation
     * @param id
     * @param newAvailability
     * @throws AccommodationDaoException
     */
    @kotlin.Throws(AccommodationDaoException::class)
    fun setAvailabilityByAccommodation(id: Int, newAvailability: Int)
}