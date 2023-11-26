package com.wanderers.hotelier_webservices.server.service.impl

import com.wanderers.hotelier_webservices.rest.model.AccommodationPatchBody
import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum
import com.wanderers.hotelier_webservices.server.component.AccommodationCache
import com.wanderers.hotelier_webservices.server.dao.api.AccommodationDao
import com.wanderers.hotelier_webservices.server.dto.AccommodationDto
import com.wanderers.hotelier_webservices.server.exception.AccommodationServiceException
import com.wanderers.hotelier_webservices.server.exception.ResultNotFoundException
import com.wanderers.hotelier_webservices.server.service.api.AccommodationService
import lombok.SneakyThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("accommodation_service")
class AccommodationServiceImpl @Autowired internal constructor(
    accommodationDao: AccommodationDao,
    accommodationCache: AccommodationCache
) :
    AccommodationService {
    private val accommodationDao: AccommodationDao
    private val accommodationCache: AccommodationCache

    init {
        this.accommodationDao = accommodationDao
        this.accommodationCache = accommodationCache
    }

    @SneakyThrows
    override fun create(accommodation: AccommodationDto?): AccommodationDto? {
        return try {
            accommodation!!.reputationBadge = getReputationBadge(accommodation.reputation)
            accommodationDao.create(accommodation)
        } catch (e: Exception) {
            throw AccommodationServiceException("Failed creating the accommodation", e)
        }
    }

    @SneakyThrows
    override fun getAccommodations(
        hotelierId: String?,
        rating: Int?,
        city: String?,
        reputationBadge: ReputationBadgeEnum?
    ): List<AccommodationDto?>? {
        return try {
            accommodationCache.getAccommodations(hotelierId!!, rating!!, city, reputationBadge)
        } catch (e: Exception) {
            throw AccommodationServiceException("Failed getting the accommodations", e)
        }
    }

    @SneakyThrows
    override fun getAccommodation(id: String?): AccommodationDto? {
        return try {
            accommodationCache.getAccommodation(id.toInt())
        } catch (e: ResultNotFoundException) {
            throw e
        } catch (e: Exception) {
            throw AccommodationServiceException("Failed getting accommodation by id", e)
        }
    }

    @SneakyThrows
    override fun patchAccommodation(id: String?, accommodationDto: AccommodationPatchBody?) {
        try {
            if (accommodationDto!!.reputation != null) {
                accommodationDao.patchAccommodation(
                    id.toInt(),
                    accommodationDto,
                    getReputationBadge(accommodationDto.reputation)
                )
            } else {
                accommodationDao.patchAccommodation(id.toInt(), accommodationDto, null)
            }
        } catch (e: Exception) {
            throw AccommodationServiceException("Failed patching accommodation by id", e)
        }
    }

    @SneakyThrows
    override fun deleteAccommodation(id: String?) {
        try {
            accommodationDao.deleteAccommodation(id.toInt())
        } catch (e: Exception) {
            throw AccommodationServiceException("Failed deleting accommodation by id", e)
        }
    }

    @SneakyThrows
    override fun getHotelierByAccommodationId(id: String?): String? {
        return try {
            accommodationDao.getHotelierByAccommodationId(id.toInt())
        } catch (e: Exception) {
            throw AccommodationServiceException("Failed retrieving hotelier by accommodation id", e)
        }
    }

    private fun getReputationBadge(reputation: Int): ReputationBadgeEnum {
        if (reputation <= 500) {
            return ReputationBadgeEnum.red
        }
        return if (reputation <= 799) ReputationBadgeEnum.yellow else ReputationBadgeEnum.green
    }
}