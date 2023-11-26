package com.wanderers.hotelier_webservices.server.service.api;

import com.wanderers.hotelier_webservices.server.exception.CustomerServiceException;

public interface CustomerService {

    /**
     * Check if the customer exist in the datasource
     *
     * @param customerId
     * @return
     * @throws CustomerServiceException
     */
    Boolean isExistingCustomer(int customerId) throws CustomerServiceException;
}
