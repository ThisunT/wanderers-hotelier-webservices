package com.wanderers.hotelier_webservices.server.service.impl

import com.wanderers.hotelier_webservices.server.dao.api.HotelierDao
import com.wanderers.hotelier_webservices.server.exception.HotelierServiceException
import com.wanderers.hotelier_webservices.server.service.api.HotelierService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("hotelier_service")
class HotelierServiceImpl @Autowired internal constructor(hotelierDao: HotelierDao) : HotelierService {
    private val hotelierDao: HotelierDao

    init {
        this.hotelierDao = hotelierDao
    }

    @kotlin.Throws(HotelierServiceException::class)
    override fun isExistingHotelier(hotelierId: String): Boolean {
        return try {
            hotelierDao.isExistingHotelier(hotelierId)
        } catch (e: Exception) {
            throw HotelierServiceException("Failed retrieving hotelier by id", e)
        }
    }
}