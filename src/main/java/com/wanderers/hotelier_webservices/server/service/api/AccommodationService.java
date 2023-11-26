package com.wanderers.hotelier_webservices.server.service.api;

import com.wanderers.hotelier_webservices.rest.model.AccommodationPatchBody;
import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum;
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto;
import com.wanderers.hotelier_webservices.server.exception.AccommodationServiceException;

import java.util.List;

/**
 * Contract that is responsible for maintaining business logic of accommodations
 */
public interface AccommodationService {

    /**
     * Create accommodation given the accommodation data
     *
     * @param accommodationDto Accommodation data transfer object
     * @return {@link AccommodationDto}
     */
    AccommodationDto create(final AccommodationDto accommodationDto) throws AccommodationServiceException;

    /**
     * Retrieve accommodations for the given criteria
     *
     * @param hotelierId
     * @param rating
     * @param city
     * @param reputationBadge
     * @return List of {@link AccommodationDto}
     */
    List<AccommodationDto> getAccommodations(String hotelierId,
                                                    Integer rating,
                                                    String city,
                                                    ReputationBadgeEnum reputationBadge)
            throws AccommodationServiceException;

    /**
     * Retrieve accommodation by id
     *
     * @param id
     * @return {@link AccommodationDto}
     */
    AccommodationDto getAccommodation(String id) throws AccommodationServiceException;

    /**
     * Update an accommodation by id with a set of modified field values
     *
     * @param id
     * @param accommodationDto
     */
    void patchAccommodation(String id, AccommodationPatchBody accommodationDto) throws AccommodationServiceException;

    /**
     * Delete an accommodation by id
     * @param id
     */
    void deleteAccommodation(String id) throws AccommodationServiceException;

    /**
     * Get hotelier id given the accommodation id
     * 
     * @param id
     * @return
     * @throws AccommodationServiceException
     */
    String getHotelierByAccommodationId(String id) throws AccommodationServiceException;
}
