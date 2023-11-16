package com.wanderers.hotelier_webservices.rest.delegate;

import com.wanderers.hotelier_webservices.mapper.AccommodationMapper;
import com.wanderers.hotelier_webservices.rest.api.AccommodationApiDelegate;
import com.wanderers.hotelier_webservices.rest.exception.HotelierIdMissingException;
import com.wanderers.hotelier_webservices.rest.exception.RESTException;
import com.wanderers.hotelier_webservices.rest.model.AccommodationRequestBody;
import com.wanderers.hotelier_webservices.rest.model.AccommodationResponseBody;
import com.wanderers.hotelier_webservices.rest.validate.AccommodationValidator;
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto;
import com.wanderers.hotelier_webservices.server.service.AccommodationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

        AccommodationResponseBody accommodationResponse = accommodationMapper.mapToResponse(accommodationResDto);

        return new ResponseEntity<>(accommodationResponse, HttpStatus.CREATED);
    }

    private String getHotelierId() {
        return Optional.ofNullable(getServletRequest().getHeader("Hotelier-Id"))
                .orElseThrow(() -> new HotelierIdMissingException("Hotelier-Id cannot be null"));
    }
}
