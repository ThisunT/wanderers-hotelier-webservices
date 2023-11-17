package com.wanderers.hotelier_webservices.mapper

import com.wanderers.hotelier_webservices.rest.model.AccommodationRequestBody
import com.wanderers.hotelier_webservices.rest.model.AccommodationResponseBody
import com.wanderers.hotelier_webservices.rest.model.Location
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors

/**
 * Class is responsible for mapping [AccommodationDto] DTO between REST and Server layers
 */
@Component("accommodation_mapper")
class AccommodationMapper {
    fun mapToDto(accommodationReq: AccommodationRequestBody, hotelierId: String?): AccommodationDto {
        val accommodationDto = AccommodationDto()
        accommodationDto.hotelierId(hotelierId)
        accommodationDto.name(accommodationReq.name)
        accommodationDto.rating(accommodationReq.rating)
        accommodationDto.category(accommodationReq.category.value.uppercase(Locale.getDefault()))
        accommodationDto.city(accommodationReq.location.city)
        accommodationDto.state(accommodationReq.location.state)
        accommodationDto.country(accommodationReq.location.country)
        accommodationDto.zipCode(accommodationReq.location.zipCode)
        accommodationDto.address(accommodationReq.location.address)
        accommodationDto.image(accommodationReq.image)
        accommodationDto.reputation(accommodationReq.reputation)
        accommodationDto.price(accommodationReq.price)
        accommodationDto.availability(accommodationReq.availability)
        return accommodationDto
    }

    fun mapToRestAccommodation(accommodationDto: AccommodationDto): AccommodationResponseBody {
        val accommodationRes = AccommodationResponseBody()
        val location = Location()
        location.city(accommodationDto.getCity())
        location.state(accommodationDto.getState())
        location.country(accommodationDto.getCountry())
        location.zipCode(accommodationDto.getZipCode())
        location.address(accommodationDto.getAddress())
        accommodationRes.id(accommodationDto.getId())
        accommodationRes.name(accommodationDto.getName())
        accommodationRes.rating(accommodationDto.getRating())
        accommodationRes.category(
            AccommodationResponseBody.CategoryEnum.fromValue(
                accommodationDto.getCategory().toLowerCase()
            )
        )
        accommodationRes.location(location)
        accommodationRes.image(accommodationDto.getImage())
        accommodationRes.reputation(accommodationDto.getReputation())
        accommodationRes.reputationBadge(accommodationDto.getReputationBadge())
        accommodationRes.price(accommodationDto.getPrice())
        accommodationRes.availability(accommodationDto.getAvailability())
        accommodationRes.name(accommodationDto.getName())
        accommodationRes.setName(accommodationDto.getName())
        return accommodationRes
    }

    fun mapToRestAccommodations(accommodationDTOs: List<AccommodationDto>): List<AccommodationResponseBody> {
        return accommodationDTOs.stream().map { accommodationDto: AccommodationDto ->
            mapToRestAccommodation(
                accommodationDto
            )
        }.collect(Collectors.toList())
    }
}

private operator fun String?.invoke(hotelierId: String?) {

}
