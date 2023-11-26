package com.wanderers.hotelier_webservices.server.service.impl;

import com.wanderers.hotelier_webservices.server.dao.api.HotelierDao;
import com.wanderers.hotelier_webservices.server.exception.HotelierServiceException;
import com.wanderers.hotelier_webservices.server.service.api.HotelierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("hotelier_service")
public class HotelierServiceImpl implements HotelierService {

    private final HotelierDao hotelierDao;

    @Autowired
    HotelierServiceImpl(HotelierDao hotelierDao) {
        this.hotelierDao = hotelierDao;
    }

    @Override
    public Boolean isExistingHotelier(String hotelierId) throws HotelierServiceException {
        try {
            return hotelierDao.isExistingHotelier(hotelierId);
        } catch (Exception e) {
            throw new HotelierServiceException("Failed retrieving hotelier by id", e);
        }
    }
}
