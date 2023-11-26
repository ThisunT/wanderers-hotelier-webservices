package com.wanderers.hotelier_webservices.server.service.impl;

import com.wanderers.hotelier_webservices.server.dao.api.CustomerDao;
import com.wanderers.hotelier_webservices.server.exception.CustomerServiceException;
import com.wanderers.hotelier_webservices.server.service.api.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("customer_service")
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;

    @Autowired
    CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public Boolean isExistingCustomer(int customerId) throws CustomerServiceException {
        try {
            return customerDao.isExistingCustomer(customerId);
        } catch (Exception e) {
            throw new CustomerServiceException("Failed retrieving customer by id", e);
        }
    }
}
