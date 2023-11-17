package com.wanderers.hotelier_webservices.rest.validate

import com.wanderers.hotelier_webservices.rest.exception.InvalidPayloadException
import com.wanderers.hotelier_webservices.rest.exception.ResourceNotFoundException
import com.wanderers.hotelier_webservices.rest.exception.UnauthorizedHotelierException
import com.wanderers.hotelier_webservices.rest.model.AccommodationPatchBody
import com.wanderers.hotelier_webservices.rest.model.AccommodationPatchBodyLocation
import com.wanderers.hotelier_webservices.rest.model.AccommodationRequestBody
import com.wanderers.hotelier_webservices.server.dao.AccommodationDao
import com.wanderers.hotelier_webservices.server.dao.HotelierDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.lang.Boolean
import java.util.*
import kotlin.NumberFormatException
import kotlin.String

@Component("accommodation_validator")
class AccommodationValidator @Autowired internal constructor(
    private val hotelierDao: HotelierDao,
    private val accommodationDao: AccommodationDao
) {
    fun validateAccommodationReq(accommodationReqBody: AccommodationRequestBody, hotelierId: String) {
        validateHotelierId(hotelierId)
        validateName(accommodationReqBody.name)
        validateZipCode(accommodationReqBody.location.zipCode)
    }

    fun validateAccommodationPatch(
        accommodationId: String,
        accommodationPatchBody: AccommodationPatchBody,
        hotelierId: String
    ) {
        validateHotelier(hotelierId, accommodationId)
        Optional.ofNullable(accommodationPatchBody.name).ifPresent { name: String ->
            validateName(
                name
            )
        }
        Optional.ofNullable(accommodationPatchBody.location)
            .flatMap { (_, _, _, zipCode): AccommodationPatchBodyLocation ->
                Optional.ofNullable(
                    zipCode
                )
            }.ifPresent { zipCode: String -> validateZipCode(zipCode) }
    }

    fun validateHotelier(hotelierId: String, accommodationId: String) {
        validateHotelierId(hotelierId)
        validateHotelierAuthority(accommodationId, hotelierId)
    }

    private fun validateHotelierAuthority(id: String, hotelierId: String) {
        val hotelierOfAccommodation = accommodationDao.getHotelierById(id.toInt())
        if (hotelierOfAccommodation != hotelierId) {
            throw UnauthorizedHotelierException("$hotelierId is not authorized to alter the record")
        }
    }

    private fun validateHotelierId(hotelierId: String) {
        if (Boolean.FALSE == hotelierDao.isExistingHotelier(hotelierId)) {
            throw ResourceNotFoundException("$hotelierId does not exists in the system. Please register!")
        }
    }

    private fun validateName(name: String) {
        val lowerCaseName: String = name.lowercase(Locale.getDefault())
        for (invalidName in INVALID_ACCOMMODATION_NAMES) {
            if (lowerCaseName.contains(invalidName)) {
                throw InvalidPayloadException("Hotel name cannot contain the word $invalidName")
            }
        }
    }

    private fun validateZipCode(zipCode: String) {
        try {
            zipCode.toInt()
        } catch (e: NumberFormatException) {
            throw InvalidPayloadException("$zipCode is not a valid integer value for zip code")
        }
    }

    companion object {
        private val INVALID_ACCOMMODATION_NAMES: Set<String> =
            HashSet(Arrays.asList("free", "offer", "book", "website"))
    }
}