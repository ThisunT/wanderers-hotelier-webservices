package com.wanderers.hotelier_webservices.rest.delegate

import com.wanderers.hotelier_webservices.rest.api.AccommodationApiDelegate
import com.wanderers.hotelier_webservices.rest.exception.ResourceNotFoundException
import com.wanderers.hotelier_webservices.rest.mapper.AccommodationMapper
import com.wanderers.hotelier_webservices.rest.model.AccommodationPatchBody
import com.wanderers.hotelier_webservices.rest.model.AccommodationRequestBody
import com.wanderers.hotelier_webservices.rest.model.AccommodationResponseBody
import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum
import com.wanderers.hotelier_webservices.rest.validate.AccommodationValidator
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto
import com.wanderers.hotelier_webservices.server.service.api.AccommodationService
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
    servletRequest: HttpServletRequest?, accommodationMapper: AccommodationMapper,
    accommodationService: AccommodationService, validator: AccommodationValidator
) : AbstractApiDelegate(servletRequest!!), AccommodationApiDelegate {
    private val validator: AccommodationValidator
    private val accommodationMapper: AccommodationMapper
    private val accommodationService: AccommodationService

    init {
        this.accommodationService = accommodationService
        this.accommodationMapper = accommodationMapper
        this.validator = validator
    }

    fun createAccommodation(accommodationRequest: AccommodationRequestBody?): ResponseEntity<AccommodationResponseBody> {
        val hotelierId = hotelierId
        validator.validateAccommodationReq(accommodationRequest!!, hotelierId)
        val accommodationReqDto = accommodationMapper.mapToAccommodationDto(accommodationRequest, hotelierId)
        val accommodationResDto: AccommodationDto = accommodationService.create(accommodationReqDto)
        val accommodationResponse = accommodationMapper.mapToRestAccommodation(accommodationResDto)
        return ResponseEntity(accommodationResponse, HttpStatus.CREATED)
    }

    override fun getAccommodations(
        hotelierId: String,
        rating: Int?,
        city: String?,
        reputationBadge: ReputationBadgeEnum?
    ): ResponseEntity<List<AccommodationResponseBody>> {
        val accommodationDTOs: List<AccommodationDto> =
            accommodationService.getAccommodations(hotelierId, rating, city, reputationBadge)
        val accommodationsResponse = accommodationMapper.mapToRestAccommodations(accommodationDTOs)
        return ResponseEntity(accommodationsResponse, HttpStatus.OK)
    }

    override fun getAccommodationById(id: String): ResponseEntity<AccommodationResponseBody> {
        val accommodationDto: AccommodationDto = accommodationService.getAccommodation(id)
        val accommodationsResponse = accommodationMapper.mapToRestAccommodation(accommodationDto)
        return ResponseEntity(accommodationsResponse, HttpStatus.OK)
    }

    fun updateAccommodationById(id: String?, body: AccommodationPatchBody?): ResponseEntity<Void> {
        val hotelierId = hotelierId
        validator.validateAccommodationPatch(id!!, body!!, hotelierId)
        accommodationService.patchAccommodation(id, body)
        return ResponseEntity(HttpStatus.OK)
    }

    fun deleteAccommodationById(id: String?): ResponseEntity<Void> {
        val hotelierId = hotelierId
        validator.validateHotelier(hotelierId, id!!)
        accommodationService.deleteAccommodation(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    private val hotelierId: String
        private get() = Optional.ofNullable(getServletRequest().getHeader("Hotelier-Id"))
            .orElseThrow {
                ResourceNotFoundException(
                    "Hotelier-Id header cannot be null"
                )
            }
}