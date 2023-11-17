package com.wanderers.hotelier_webservices.server.service;

import com.wanderers.hotelier_webservices.rest.model.AccommodationPatchBody;
import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum;
import com.wanderers.hotelier_webservices.server.AccommodationCache;
import com.wanderers.hotelier_webservices.server.dao.AccommodationDao;
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto;
import com.wanderers.hotelier_webservices.server.exception.AccommodationServiceException;
import com.wanderers.hotelier_webservices.server.exception.ResultNotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class is responsible for maintaining business logic of accommodations
 */
@Service("accommodation_service")
public class AccommodationService {

    private final AccommodationDao accommodationDao;
    private final AccommodationCache accommodationCache;

    @Autowired
    AccommodationService(AccommodationDao accommodationDao, AccommodationCache accommodationCache) {
        this.accommodationDao = accommodationDao;
        this.accommodationCache = accommodationCache;
    }

    @SneakyThrows
    public AccommodationDto create(AccommodationDto accommodation) {
        try {
            accommodation.setReputationBadge(getReputationBadge(accommodation.getReputation()));

            return accommodationDao.create(accommodation);
        } catch (Exception e) {
            throw new AccommodationServiceException("Failed creating the accommodation", e);
        }
    }

    @SneakyThrows
    public List<AccommodationDto> getAccommodations(String hotelierId,
                                                    Integer rating,
                                                    String city,
                                                    ReputationBadgeEnum reputationBadge) {
        try {
            return accommodationCache.getAccommodations(hotelierId, rating, city, reputationBadge);
        } catch (Exception e) {
            throw new AccommodationServiceException("Failed getting the accommodations", e);
        }
    }

    @SneakyThrows
    public AccommodationDto getAccommodation(String id) {
        try {
            return accommodationCache.getAccommodation(Integer.parseInt(id));
        } catch (ResultNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new AccommodationServiceException("Failed getting accommodation by id", e);
        }
    }

    @SneakyThrows
    public void patchAccommodation(String id, AccommodationPatchBody accommodationDto) {
        try {
            if (accommodationDto.getReputation() != null) {
                accommodationDao.patchAccommodation(Integer.parseInt(id), accommodationDto, getReputationBadge(accommodationDto.getReputation()));
            } else {
                accommodationDao.patchAccommodation(Integer.parseInt(id), accommodationDto, null);
            }

        } catch (Exception e) {
            throw new AccommodationServiceException("Failed patching accommodation by id", e);
        }
    }

    @SneakyThrows
    public void deleteAccommodation(String id) {
        try {
            accommodationDao.deleteAccommodation(Integer.parseInt(id));
        } catch (Exception e) {
            throw new AccommodationServiceException("Failed deleting accommodation by id", e);
        }
    }

    private ReputationBadgeEnum getReputationBadge(int reputation) {
        if (reputation <= 500) {
            return ReputationBadgeEnum.red;
        }
        return reputation <= 799 ? ReputationBadgeEnum.yellow : ReputationBadgeEnum.green;
    }

}
