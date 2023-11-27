package com.wanderers.hotelier_webservices.server.dao.api

import com.wanderers.hotelier_webservices.server.exception.HotelierDaoException

/**
 * Data access contract for the hotelier entity
 */
interface HotelierDao {

    /**
     * Check if the hotelier exists in the datasource
     *
     * @param hotelierId
     * @return
     * @throws HotelierDaoException
     */
    @kotlin.Throws(HotelierDaoException::class)
    fun isExistingHotelier(hotelierId: String): Boolean
}