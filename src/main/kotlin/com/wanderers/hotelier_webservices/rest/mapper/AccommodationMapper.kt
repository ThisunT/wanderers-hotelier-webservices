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
    fun mapToAccommodationDto(accommodationReq: AccommodationRequestBody, hotelierId: String): AccommodationDto {
        return AccommodationDto(
            null,
            hotelierId,
            accommodationReq.name,
            accommodationReq.rating,
            accommodationReq.category.value.uppercase(Locale.getDefault()),
            accommodationReq.location.city,
            accommodationReq.location.state,
            accommodationReq.location.country,
            accommodationReq.location.zipCode,
            accommodationReq.location.address,
            accommodationReq.image,
            accommodationReq.reputation,
            null,
            accommodationReq.price,
            accommodationReq.availability
        )
    }

    fun mapToRestAccommodation(accommodationDto: AccommodationDto): AccommodationResponseBody {

        val location = Location(
            accommodationDto.city,
            accommodationDto.state,
            accommodationDto.country,
            accommodationDto.zipCode,
            accommodationDto.address
        )

        return AccommodationResponseBody(
            accommodationDto.id!!,
            accommodationDto.reputationBadge!!,
            location,
            accommodationDto.name,
            accommodationDto.rating,
            AccommodationResponseBody.Category.valueOf(accommodationDto.category.lowercase()),
            accommodationDto.image,
            accommodationDto.reputation,
            accommodationDto.price,
            accommodationDto.availability
        )
    }

    fun mapToRestAccommodations(accommodationDTOs: List<AccommodationDto>): List<AccommodationResponseBody> {
        return accommodationDTOs.stream().map { accommodationDto: AccommodationDto ->
            mapToRestAccommodation(
                accommodationDto
            )
        }.collect(Collectors.toList())
    }
}