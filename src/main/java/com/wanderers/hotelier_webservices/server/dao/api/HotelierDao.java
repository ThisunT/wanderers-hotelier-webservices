package com.wanderers.hotelier_webservices.server.dao.api;

import com.wanderers.hotelier_webservices.server.exception.HotelierDaoException;

/**
 * Data access contract for the hotelier entity
 */
public interface HotelierDao {

    /**
     * Check if the hotelier exists in the datasource
     *
     * @param hotelierId
     * @return
     * @throws HotelierDaoException
     */
    Boolean isExistingHotelier(String hotelierId) throws HotelierDaoException;
}
