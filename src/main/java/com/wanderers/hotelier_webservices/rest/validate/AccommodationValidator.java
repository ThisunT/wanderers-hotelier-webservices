package com.wanderers.hotelier_webservices.rest.validate;

import com.wanderers.hotelier_webservices.rest.exception.UnauthorizedHotelierException;
import com.wanderers.hotelier_webservices.rest.exception.InvalidPayloadException;
import com.wanderers.hotelier_webservices.rest.exception.ResourceNotFoundException;
import com.wanderers.hotelier_webservices.rest.model.AccommodationPatchBody;
import com.wanderers.hotelier_webservices.rest.model.AccommodationRequestBody;
import com.wanderers.hotelier_webservices.server.service.api.AccommodationService;
import com.wanderers.hotelier_webservices.server.service.api.HotelierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component("accommodation_validator")
public class AccommodationValidator {

    private static final Set<String> INVALID_ACCOMMODATION_NAMES = new HashSet<>(Arrays.asList("free", "offer", "book", "website"));

    private final HotelierService hotelierService;
    private final AccommodationService accommodationService;

    @Autowired
    AccommodationValidator(HotelierService hotelierService, AccommodationService accommodationService) {
        this.hotelierService = hotelierService;
        this.accommodationService = accommodationService;
    }

    public void validateAccommodationReq(AccommodationRequestBody accommodationReqBody, String hotelierId) {
        validateHotelierId(hotelierId);
        validateName(accommodationReqBody.getName());
        validateZipCode(accommodationReqBody.getLocation().getZipCode());
    }

    public void validateAccommodationPatch(String accommodationId, AccommodationPatchBody accommodationPatchBody, String hotelierId) {
        validateHotelier(hotelierId, accommodationId);
        Optional.ofNullable(accommodationPatchBody.getName()).ifPresent(this::validateName);
        Optional.ofNullable(accommodationPatchBody.getLocation()).flatMap(val -> Optional.ofNullable(val.getZipCode())).ifPresent(this::validateZipCode);
    }

    public void validateHotelier(String hotelierId, String accommodationId) {
        validateHotelierId(hotelierId);
        validateHotelierAuthority(accommodationId, hotelierId);
    }

    private void validateHotelierAuthority(String id, String hotelierId) {
        String hotelierOfAccommodation = accommodationService.getHotelierByAccommodationId(id);

        if(!hotelierOfAccommodation.equals(hotelierId)) {
            throw new UnauthorizedHotelierException("Hotelier: " + hotelierId + " is not authorized to alter the record");
        }
    }

    private void validateHotelierId(String hotelierId) {
        if (Boolean.FALSE.equals(hotelierService.isExistingHotelier(hotelierId))) {
            throw new ResourceNotFoundException("Hotelier: " + hotelierId + " does not exists in the system. Please register!");
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
