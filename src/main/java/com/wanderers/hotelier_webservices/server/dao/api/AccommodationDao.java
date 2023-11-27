package com.wanderers.hotelier_webservices.server.dao.api;

import com.wanderers.hotelier_webservices.server.dto.AccommodationDto;
import com.wanderers.hotelier_webservices.server.dto.ReputationBadge;
import com.wanderers.hotelier_webservices.server.exception.AccommodationDaoException;
import com.wanderers.hotelier_webservices.server.exception.ResultNotFoundException;

import java.util.List;

/**
 * Data access contract for the accommodation entity
 */
public interface AccommodationDao {

    /**
     * Creates an accommodation entity and a location entity
     * 
     * @param accommodationDto
     * @return {@link AccommodationDto}
     * @throws AccommodationDaoException
     */
    AccommodationDto create(AccommodationDto accommodationDto) throws AccommodationDaoException;

    /**
     * Get accommodation entities by a given criteria
     * 
     * @param hotelierId
     * @param rating
     * @param city
     * @param reputationBadge
     * @return a list of {@link AccommodationDto}
     * @throws AccommodationDaoException
     */
    List<AccommodationDto> getAccommodations(String hotelierId, Integer rating,
                                                    String city, ReputationBadge reputationBadge)
            throws AccommodationDaoException;

    /**
     * Get an accommodation by id
     * 
     * @param id
     * @return {@link AccommodationDto}
     * @throws AccommodationDaoException
     * @throws ResultNotFoundException
     */
    AccommodationDto getAccommodation(int id) throws AccommodationDaoException, ResultNotFoundException;

    /**
     * Get a hotelier id given an accommodation id
     * 
     * @param id
     * @return Hotelier Id
     */
    String getHotelierByAccommodationId(int id) throws ResultNotFoundException, AccommodationDaoException;

    /**
     * Update accommodation by provided new field values
     * 
     * @param id
     * @param patchDTO
     * @param reputationBadge
     * @throws AccommodationDaoException
     */
    void patchAccommodation(int id, AccommodationDto accommodationDto, ReputationBadge reputationBadge) throws AccommodationDaoException;

    /**
     * Delete an accommodation
     * @param id
     */
    void deleteAccommodation(int id) throws AccommodationDaoException;

    /**
     * Get the availability of an accommodation
     * 
     * @param id
     * @return
     * @throws AccommodationDaoException
     * @throws ResultNotFoundException
     */
    Integer getAvailabilityByAccommodation(int id) throws AccommodationDaoException, ResultNotFoundException;

    /**
     * Set the updated availability of an accommodation
     * @param id
     * @param newAvailability
     * @throws AccommodationDaoException
     */
    void setAvailabilityByAccommodation(int id, int newAvailability) throws AccommodationDaoException;

}
