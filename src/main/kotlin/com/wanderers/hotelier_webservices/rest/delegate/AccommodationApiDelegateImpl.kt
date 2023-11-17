package com.wanderers.hotelier_webservices.rest.delegate

import com.wanderers.hotelier_webservices.mapper.AccommodationMapper
import com.wanderers.hotelier_webservices.rest.api.AccommodationApiDelegate
import com.wanderers.hotelier_webservices.rest.exception.ResourceNotFoundException
import com.wanderers.hotelier_webservices.rest.model.AccommodationPatchBody
import com.wanderers.hotelier_webservices.rest.model.AccommodationRequestBody
import com.wanderers.hotelier_webservices.rest.model.AccommodationResponseBody
import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum
import com.wanderers.hotelier_webservices.rest.validate.AccommodationValidator
import com.wanderers.hotelier_webservices.server.service.AccommodationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * Class implements the endpoints of accommodation resource.
 */
@Service("accommodation_api_delegate")
class AccommodationApiDelegateImpl @Autowired internal constructor(
    servletRequest: HttpServletRequest?, private val accommodationMapper: AccommodationMapper,
    private val accommodationService: AccommodationService, private val validator: AccommodationValidator
) : AbstractApiDelegate(servletRequest), AccommodationApiDelegate {

    override fun createAccommodation(
        hotelierId: String,
        accommodationRequest: AccommodationRequestBody
    ): ResponseEntity<AccommodationResponseBody> {
        validator.validateAccommodationReq(accommodationRequest, hotelierId)
        val accommodationReqDto = accommodationMapper.mapToDto(accommodationRequest, hotelierId)
        val accommodationResDto = accommodationService.create(accommodationReqDto)
        val accommodationResponse = accommodationMapper.mapToRestAccommodation(accommodationResDto)
        return ResponseEntity(accommodationResponse, HttpStatus.CREATED)
    }

    override fun getAccommodations(
        hotelierId: String,
        rating: Int?,
        city: String?,
        reputationBadge: ReputationBadgeEnum?
    ): ResponseEntity<List<AccommodationResponseBody>> {
        val accommodationDTOs = accommodationService.getAccommodations(hotelierId, rating, city, reputationBadge)
        val accommodationsResponse = accommodationMapper.mapToRestAccommodations(accommodationDTOs)
        return ResponseEntity(accommodationsResponse, HttpStatus.OK)
    }

    override fun getAccommodationById(id: String): ResponseEntity<AccommodationResponseBody> {
        val accommodationDto = accommodationService.getAccommodation(id)
        val accommodationsResponse = accommodationMapper.mapToRestAccommodation(accommodationDto)
        return ResponseEntity(accommodationsResponse, HttpStatus.OK)
    }

    override fun updateAccommodationById(
        hotelierId: String,
        id: String,
        accommodationPatchBody: AccommodationPatchBody
    ): ResponseEntity<Unit> {
        validator.validateAccommodationPatch(id, accommodationPatchBody, hotelierId)
        accommodationService.patchAccommodation(id, accommodationPatchBody)
        return ResponseEntity(HttpStatus.OK)
    }

    override fun deleteAccommodationById(hotelierId: String, id: String): ResponseEntity<Unit> {
        validator.validateHotelier(hotelierId, id)
        accommodationService.deleteAccommodation(id)
        return ResponseEntity(HttpStatus.OK)
    }

    private val hotelierId: String
        get() = Optional.ofNullable(getServletRequest().getHeader("Hotelier-Id"))
            .orElseThrow {
                ResourceNotFoundException(
                    "Hotelier-Id header cannot be null"
                )
            }
}