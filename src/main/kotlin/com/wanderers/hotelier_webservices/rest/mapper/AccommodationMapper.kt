package com.wanderers.hotelier_webservices.rest.mapper

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
    fun mapToAccommodationDto(accommodationReq: AccommodationRequestBody, hotelierId: String?): AccommodationDto {
        val accommodationDto = AccommodationDto()
        accommodationDto.hotelierId = hotelierId
        accommodationDto.name = accommodationReq.name
        accommodationDto.rating = accommodationReq.rating
        accommodationDto.category = accommodationReq.category.value.uppercase(Locale.getDefault())
        accommodationDto.city = accommodationReq.location.city
        accommodationDto.state = accommodationReq.location.state
        accommodationDto.country = accommodationReq.location.country
        accommodationDto.zipCode = accommodationReq.location.zipCode
        accommodationDto.address = accommodationReq.location.address
        accommodationDto.image = accommodationReq.image
        accommodationDto.reputation = accommodationReq.reputation
        accommodationDto.price = accommodationReq.price
        accommodationDto.availability = accommodationReq.availability
        return accommodationDto
    }

    fun mapToRestAccommodation(accommodationDto: AccommodationDto): AccommodationResponseBody {
        val accommodationRes = AccommodationResponseBody()
        val location = Location()
        location.setCity(accommodationDto.city)
        location.setState(accommodationDto.state)
        location.setCountry(accommodationDto.country)
        location.setZipCode(accommodationDto.zipCode)
        location.setAddress(accommodationDto.address)
        accommodationRes.setId(accommodationDto.id)
        accommodationRes.setName(accommodationDto.name)
        accommodationRes.setRating(accommodationDto.rating)
        accommodationRes.setCategory(
            AccommodationResponseBody.CategoryEnum.fromValue(
                accommodationDto.category.lowercase(
                    Locale.getDefault()
                )
            )
        )
        accommodationRes.setLocation(location)
        accommodationRes.setImage(accommodationDto.image)
        accommodationRes.setReputation(accommodationDto.reputation)
        accommodationRes.setReputationBadge(accommodationDto.reputationBadge)
        accommodationRes.setPrice(accommodationDto.price)
        accommodationRes.setAvailability(accommodationDto.availability)
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