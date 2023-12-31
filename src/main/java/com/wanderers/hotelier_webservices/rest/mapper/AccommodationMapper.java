package com.wanderers.hotelier_webservices.rest.mapper;

import com.wanderers.hotelier_webservices.rest.model.*;
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto;
import com.wanderers.hotelier_webservices.server.dto.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class is responsible for mapping {@link AccommodationDto} DTO between REST and Server layers
 */
@Component("accommodation_mapper")
public class AccommodationMapper {

    public AccommodationDto mapToAccommodationDto(AccommodationRequestBody accommodationReq, String hotelierId) {
        final AccommodationDto accommodationDto = new AccommodationDto();

        accommodationDto.setHotelierId(hotelierId);
        accommodationDto.setName(accommodationReq.getName());
        accommodationDto.setRating(accommodationReq.getRating());
        accommodationDto.setCategory(Category.fromValue(accommodationReq.getCategory().getValue()));
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

    public AccommodationDto mapToAccommodationDto(AccommodationPatchBody accommodationPatch, String hotelierId) {
        final AccommodationDto accommodationDto = new AccommodationDto();

        accommodationDto.setHotelierId(hotelierId);
        accommodationDto.setName(accommodationPatch.getName());
        accommodationDto.setRating(accommodationPatch.getRating());
        accommodationDto.setCategory(Optional.ofNullable(accommodationPatch.getCategory()).map(cat -> Category.fromValue(cat.getValue())).orElse(null));
        accommodationDto.setImage(accommodationPatch.getImage());
        accommodationDto.setReputation(accommodationPatch.getReputation());
        accommodationDto.setPrice(accommodationPatch.getPrice());
        accommodationDto.setAvailability(accommodationPatch.getAvailability());

        OptionalLocation location = accommodationPatch.getLocation();
        if (location != null) {
            accommodationDto.setCity(location.getCity());
            accommodationDto.setState(location.getState());
            accommodationDto.setCountry(location.getCountry());
            accommodationDto.setZipCode(location.getZipCode());
            accommodationDto.setAddress(location.getAddress());
        }

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
        accommodationRes.setCategory(AccommodationResponseBody.CategoryEnum.fromValue(accommodationDto.getCategory().value));
        accommodationRes.setLocation(location);
        accommodationRes.setImage(accommodationDto.getImage());
        accommodationRes.setReputation(accommodationDto.getReputation());
        accommodationRes.setReputationBadge(ReputationBadgeEnum.fromValue(accommodationDto.getReputationBadge().value));
        accommodationRes.setPrice(accommodationDto.getPrice());
        accommodationRes.setAvailability(accommodationDto.getAvailability());

        return accommodationRes;
    }

    public List<AccommodationResponseBody> mapToRestAccommodations(List<AccommodationDto> accommodationDTOs) {
        return accommodationDTOs.stream().map(this::mapToRestAccommodation).collect(Collectors.toList());
    }
}
