package com.wanderers.hotelier_webservices.rest.validate;

import com.wanderers.hotelier_webservices.rest.exception.InvalidHotelierException;
import com.wanderers.hotelier_webservices.rest.exception.InvalidPayloadException;
import com.wanderers.hotelier_webservices.rest.model.AccommodationPatchBody;
import com.wanderers.hotelier_webservices.rest.model.AccommodationRequestBody;
import com.wanderers.hotelier_webservices.server.dao.AccommodationDao;
import com.wanderers.hotelier_webservices.server.dao.HotelierDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component("accommodation_validator")
public class AccommodationValidator {

    private static final Set<String> INVALID_ACCOMMODATION_NAMES = new HashSet<>(Arrays.asList("free", "offer", "book", "website"));

    private final HotelierDao hotelierDao;
    private final AccommodationDao accommodationDao;

    @Autowired
    AccommodationValidator(HotelierDao hotelierDao, AccommodationDao accommodationDao) {
        this.hotelierDao = hotelierDao;
        this.accommodationDao = accommodationDao;
    }

    public void validateAccommodationReq(AccommodationRequestBody accommodationReqBody, String hotelierId) {
        validateName(accommodationReqBody.getName());
        validateZipCode(accommodationReqBody.getLocation().getZipCode());
        validateHotelierId(hotelierId);
    }

    public void validateAccommodationPatch(String id, AccommodationPatchBody accommodationPatchBody, String hotelierId) {
        validateHotelierId(hotelierId);
        validateHotelierAuthority(id, hotelierId);
        Optional.of(accommodationPatchBody.getName()).ifPresent(this::validateName);
        Optional.of(accommodationPatchBody.getLocation().getZipCode()).ifPresent(this::validateZipCode);
    }

    public void validateHotelierAuthority(String id, String hotelierId) {
        String hotelierOfAccommodation = accommodationDao.getHotelierById(Integer.parseInt(id));

        if(!hotelierOfAccommodation.equals(hotelierId)) {
            throw new InvalidHotelierException(hotelierId + " is not authorized to edit the record");
        }
    }

    public void validateHotelierId(String hotelierId) {
        if (Boolean.FALSE.equals(hotelierDao.isExistingHotelier(hotelierId))) {
            throw new InvalidHotelierException(hotelierId + " does not exists in the system. Please register");
        }
    }

    private void validateName(String name) {
        String lowerCaseName = name.toLowerCase();

        for (String invalidName : INVALID_ACCOMMODATION_NAMES) {
            if (lowerCaseName.contains(invalidName)) {
                throw new InvalidPayloadException("Hotel name cannot contain the word " + invalidName);
            }
        }
    }

    private void validateZipCode(String zipCode) {
        try {
            Integer.parseInt(zipCode);
        } catch (NumberFormatException e) {
            throw new InvalidPayloadException(zipCode + " is not a valid integer value for zip code");
        }
    }
}
