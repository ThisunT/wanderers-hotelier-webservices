package com.wanderers.hotelier_webservices.rest.delegate;

import com.wanderers.hotelier_webservices.rest.api.AccommodationApiDelegate;
import com.wanderers.hotelier_webservices.rest.exception.ResourceNotFoundException;
import com.wanderers.hotelier_webservices.rest.mapper.AccommodationMapper;
import com.wanderers.hotelier_webservices.rest.model.AccommodationPatchBody;
import com.wanderers.hotelier_webservices.rest.model.AccommodationRequestBody;
import com.wanderers.hotelier_webservices.rest.model.AccommodationResponseBody;
import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum;
import com.wanderers.hotelier_webservices.rest.validate.AccommodationValidator;
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto;
import com.wanderers.hotelier_webservices.server.dto.ReputationBadge;
import com.wanderers.hotelier_webservices.server.service.api.AccommodationService;
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

        AccommodationDto accommodationReqDto = accommodationMapper.mapToAccommodationDto(accommodationRequest, hotelierId);
        AccommodationDto accommodationResDto = accommodationService.create(accommodationReqDto);
        AccommodationResponseBody accommodationResponse = accommodationMapper.mapToRestAccommodation(accommodationResDto);

        return new ResponseEntity<>(accommodationResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<AccommodationResponseBody>> getAccommodations(String hotelierId,
                                                                             Integer rating,
                                                                             String city,
                                                                             ReputationBadgeEnum reputationBadge) {

        List<AccommodationDto> accommodationDTOs = accommodationService.getAccommodations(hotelierId, rating,
                city, ReputationBadge.fromValue(reputationBadge.getValue()));
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

        AccommodationDto accommodationReqDto = accommodationMapper.mapToAccommodationDto(body, hotelierId);
        accommodationService.patchAccommodation(id, accommodationReqDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteAccommodationById(String id) {
        var hotelierId = getHotelierId();
        validator.validateHotelier(hotelierId, id);

        accommodationService.deleteAccommodation(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private String getHotelierId() {
        return Optional.ofNullable(getServletRequest().getHeader("Hotelier-Id"))
                .orElseThrow(() -> new ResourceNotFoundException("Hotelier-Id header cannot be null"));
    }
}
