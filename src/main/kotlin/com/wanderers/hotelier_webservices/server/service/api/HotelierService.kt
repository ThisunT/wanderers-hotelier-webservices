package com.wanderers.hotelier_webservices.server.service.api

import com.wanderers.hotelier_webservices.server.exception.HotelierServiceException

interface HotelierService {
    /**
     * Check if the hotelier exist in the datasource
     *
     * @param hotelierId
     * @return
     * @throws HotelierServiceException
     */
    @kotlin.Throws(HotelierServiceException::class)
    fun isExistingHotelier(hotelierId: String?): Boolean?
}