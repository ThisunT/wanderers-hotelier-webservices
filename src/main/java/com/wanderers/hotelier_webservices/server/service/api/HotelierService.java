package com.wanderers.hotelier_webservices.server.service.api;

import com.wanderers.hotelier_webservices.server.exception.HotelierServiceException;

public interface HotelierService {

    /**
     * Check if the hotelier exist in the datasource
     *
     * @param hotelierId
     * @return
     * @throws HotelierServiceException
     */
    Boolean isExistingHotelier(String hotelierId) throws HotelierServiceException;
}
