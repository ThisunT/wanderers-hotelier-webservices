package com.wanderers.hotelier_webservices.server.service;

import com.wanderers.hotelier_webservices.rest.model.AccommodationResponseBody.ReputationBadgeEnum;
import com.wanderers.hotelier_webservices.server.dao.AccommodationDao;
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto;
import com.wanderers.hotelier_webservices.server.exception.AccommodationServiceException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class is responsible for maintaining business logic of accommodations
 */
@Service("accommodation_service")
public class AccommodationService {

    private final AccommodationDao accommodationDao;

    @Autowired
    AccommodationService(AccommodationDao accommodationDao) {
        this.accommodationDao = accommodationDao;
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

    private ReputationBadgeEnum getReputationBadge(int reputation) {
        if (reputation <= 500) {
            return ReputationBadgeEnum.RED;
        }
        return reputation <= 799 ? ReputationBadgeEnum.YELLOW : ReputationBadgeEnum.GREEN;
    }

}
