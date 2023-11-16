package com.wanderers.hotelier_webservices.mapper;

import com.wanderers.hotelier_webservices.rest.model.AccommodationRequestBody;
import com.wanderers.hotelier_webservices.rest.model.AccommodationResponseBody;
import com.wanderers.hotelier_webservices.rest.model.Location;
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class is responsible for mapping {@link AccommodationDto} DTO between REST and Server layers
 */
@Component("accommodation_mapper")
public class AccommodationMapper {

    public AccommodationDto mapToDto(AccommodationRequestBody accommodationReq, String hotelierId) {
        final AccommodationDto accommodationDto = new AccommodationDto();

        accommodationDto.setHotelierId(hotelierId);
        accommodationDto.setName(accommodationReq.getName());
        accommodationDto.setRating(accommodationReq.getRating());
        accommodationDto.setCategory(accommodationReq.getCategory().getValue().toUpperCase());
        accommodationDto.setCity(accommodationReq.getLocation().getCity());
        accommodationDto.setState(accommodationReq.getLocation().getState());
        accommodationDto.setCountry(accommodationReq.getLocation().getCountry());
        accommodationDto.setZipCode(accommodationReq.getLocation().getZipCode());
        accommodationDto.setAddress(accommodationReq.getLocation().getAddress());
        accommodationDto.setImage(accommodationReq.getImage());
        accommodationDto.setReputation(accommodationReq.getReputation());
        accommodationDto.setPrice(accommodationReq.getPrice());
        accommodationDto.setAvailability(accommodationReq.getAvailability());

        return accommodationDto;
    }

    public AccommodationResponseBody mapToRestAccommodation(AccommodationDto accommodationDto) {
        final AccommodationResponseBody accommodationRes = new AccommodationResponseBody();
        final Location location = new Location();

        location.setCity(accommodationDto.getCity());
        location.setState(accommodationDto.getState());
        location.setCountry(accommodationDto.getCountry());
        location.setZipCode(accommodationDto.getZipCode());
        location.setAddress(accommodationDto.getAddress());

        accommodationRes.setId(accommodationDto.getId());
        accommodationRes.setName(accommodationDto.getName());
        accommodationRes.setRating(accommodationDto.getRating());
        accommodationRes.setCategory(AccommodationResponseBody.CategoryEnum.fromValue(accommodationDto.getCategory().toLowerCase()));
        accommodationRes.setLocation(location);
        accommodationRes.setImage(accommodationDto.getImage());
        accommodationRes.setReputation(accommodationDto.getReputation());
        accommodationRes.setReputationBadge(accommodationDto.getReputationBadge());
        accommodationRes.setPrice(accommodationDto.getPrice());
        accommodationRes.setAvailability(accommodationDto.getAvailability());
        accommodationRes.setName(accommodationDto.getName());
        accommodationRes.setName(accommodationDto.getName());

        return accommodationRes;
    }

    public List<AccommodationResponseBody> mapToRestAccommodations(List<AccommodationDto> accommodationDTOs) {
        return accommodationDTOs.stream().map(this::mapToRestAccommodation).collect(Collectors.toList());
    }
}
