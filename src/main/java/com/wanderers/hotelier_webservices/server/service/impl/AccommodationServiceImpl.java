package com.wanderers.hotelier_webservices.server.service.impl;

import com.wanderers.hotelier_webservices.server.component.AccommodationCache;
import com.wanderers.hotelier_webservices.server.dao.api.AccommodationDao;
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto;
import com.wanderers.hotelier_webservices.server.dto.ReputationBadge;
import com.wanderers.hotelier_webservices.server.exception.AccommodationServiceException;
import com.wanderers.hotelier_webservices.server.exception.ResultNotFoundException;
import com.wanderers.hotelier_webservices.server.service.api.AccommodationService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.wanderers.hotelier_webservices.server.dto.ReputationBadge.*;

@Service("accommodation_service")
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationDao accommodationDao;
    private final AccommodationCache accommodationCache;

    @Autowired
    AccommodationServiceImpl(AccommodationDao accommodationDao, AccommodationCache accommodationCache) {
        this.accommodationDao = accommodationDao;
        this.accommodationCache = accommodationCache;
    }

    @SneakyThrows
    @Override
    public AccommodationDto create(AccommodationDto accommodation) {
        try {
            accommodation.setReputationBadge(getReputationBadge(accommodation.getReputation()));

            return accommodationDao.create(accommodation);
        } catch (Exception e) {
            throw new AccommodationServiceException("Failed creating the accommodation", e);
        }
    }

    @SneakyThrows
    @Override
    public List<AccommodationDto> getAccommodations(String hotelierId,
                                                    Integer rating,
                                                    String city,
                                                    ReputationBadge reputationBadge) {
        try {
            return accommodationCache.getAccommodations(hotelierId, rating, city, reputationBadge);
        } catch (Exception e) {
            throw new AccommodationServiceException("Failed getting the accommodations", e);
        }
    }

    @SneakyThrows
    @Override
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
    @Override
    public void patchAccommodation(String id, AccommodationDto accommodationDto) {
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
    @Override
    public void deleteAccommodation(String id) {
        try {
            accommodationDao.deleteAccommodation(Integer.parseInt(id));
        } catch (Exception e) {
            throw new AccommodationServiceException("Failed deleting accommodation by id", e);
        }
    }

    @SneakyThrows
    @Override
    public String getHotelierByAccommodationId(String id) {
        try {
            return accommodationDao.getHotelierByAccommodationId(Integer.parseInt(id));
        } catch (ResultNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new AccommodationServiceException("Failed retrieving hotelier by accommodation id", e);
        }
    }

    private ReputationBadge getReputationBadge(int reputation) {
        if (reputation <= 500) {
            return RED;
        }
        return reputation <= 799 ? YELLOW : GREEN;
    }

}
