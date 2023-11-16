package com.wanderers.hotelier_webservices.rest.delegate;

import com.wanderers.hotelier_webservices.mapper.AccommodationMapper;
import com.wanderers.hotelier_webservices.rest.api.AccommodationApiDelegate;
import com.wanderers.hotelier_webservices.rest.exception.HotelierIdMissingException;
import com.wanderers.hotelier_webservices.rest.model.AccommodationPatchBody;
import com.wanderers.hotelier_webservices.rest.model.AccommodationRequestBody;
import com.wanderers.hotelier_webservices.rest.model.AccommodationResponseBody;
import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum;
import com.wanderers.hotelier_webservices.rest.validate.AccommodationValidator;
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto;
import com.wanderers.hotelier_webservices.server.service.AccommodationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * Class implements the endpoints of accommodation resource.
 */
@Service("accommodation_api_delegate")
public class AccommodationApiDelegateImpl extends AbstractApiDelegate implements AccommodationApiDelegate {

    private final AccommodationValidator validator;
    private final AccommodationMapper accommodationMapper;
    private final AccommodationService accommodationService;

    @Autowired
    AccommodationApiDelegateImpl(HttpServletRequest servletRequest, AccommodationMapper accommodationMapper,
                                 AccommodationService accommodationService, AccommodationValidator validator) {
        super(servletRequest);
        this.accommodationService = accommodationService;
        this.accommodationMapper = accommodationMapper;
        this.validator = validator;
    }

    @Override
    public ResponseEntity<AccommodationResponseBody> createAccommodation(AccommodationRequestBody accommodationRequest) {

        var hotelierId = getHotelierId();

        validator.validateAccommodationReq(accommodationRequest, hotelierId);

        AccommodationDto accommodationReqDto = accommodationMapper.mapToDto(accommodationRequest, hotelierId);

        AccommodationDto accommodationResDto = accommodationService.create(accommodationReqDto);

        AccommodationResponseBody accommodationResponse = accommodationMapper.mapToRestAccommodation(accommodationResDto);

        return new ResponseEntity<>(accommodationResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<AccommodationResponseBody>> getAccommodations(String hotelierId,
                                                                             Integer rating,
                                                                             String city,
                                                                             ReputationBadgeEnum reputationBadge) {

        List<AccommodationDto> accommodationDTOs = accommodationService.getAccommodations(hotelierId, rating, city, reputationBadge);

        List<AccommodationResponseBody> accommodationsResponse = accommodationMapper.mapToRestAccommodations(accommodationDTOs);

        return new ResponseEntity<>(accommodationsResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AccommodationResponseBody> getAccommodationById(String id) {

        AccommodationDto accommodationDto = accommodationService.getAccommodation(id);

        AccommodationResponseBody accommodationsResponse = accommodationMapper.mapToRestAccommodation(accommodationDto);

        return new ResponseEntity<>(accommodationsResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateAccommodationById(String id, AccommodationPatchBody body) {
        var hotelierId = getHotelierId();

        validator.validateAccommodationPatch(id, body, hotelierId);

        accommodationService.patchAccommodation(id, body);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String getHotelierId() {
        return Optional.ofNullable(getServletRequest().getHeader("Hotelier-Id"))
                .orElseThrow(() -> new HotelierIdMissingException("Hotelier-Id cannot be null"));
    }
}
