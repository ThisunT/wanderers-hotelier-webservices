package com.wanderers.hotelier_webservices.server.service.api

import com.wanderers.hotelier_webservices.rest.model.AccommodationPatchBody
import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto
import com.wanderers.hotelier_webservices.server.exception.AccommodationServiceException

/**
 * Contract that is responsible for maintaining business logic of accommodations
 */
interface AccommodationService {
    /**
     * Create accommodation given the accommodation data
     *
     * @param accommodationDto Accommodation data transfer object
     * @return [AccommodationDto]
     */
    @kotlin.Throws(AccommodationServiceException::class)
    fun create(accommodationDto: AccommodationDto): AccommodationDto

    /**
     * Retrieve accommodations for the given criteria
     *
     * @param hotelierId
     * @param rating
     * @param city
     * @param reputationBadge
     * @return List of [AccommodationDto]
     */
    @kotlin.Throws(AccommodationServiceException::class)
    fun getAccommodations(
        hotelierId: String,
        rating: Int?,
        city: String?,
        reputationBadge: ReputationBadgeEnum?
    ): List<AccommodationDto>

    /**
     * Retrieve accommodation by id
     *
     * @param id
     * @return [AccommodationDto]
     */
    @kotlin.Throws(AccommodationServiceException::class)
    fun getAccommodation(id: String): AccommodationDto

    /**
     * Update an accommodation by id with a set of modified field values
     *
     * @param id
     * @param accommodationDto
     */
    @kotlin.Throws(AccommodationServiceException::class)
    fun patchAccommodation(id: String, accommodationDto: AccommodationPatchBody)

    /**
     * Delete an accommodation by id
     * @param id
     */
    @kotlin.Throws(AccommodationServiceException::class)
    fun deleteAccommodation(id: String)

    /**
     * Get hotelier id given the accommodation id
     *
     * @param id
     * @return
     * @throws AccommodationServiceException
     */
    @kotlin.Throws(AccommodationServiceException::class)
    fun getHotelierByAccommodationId(id: String): String
}