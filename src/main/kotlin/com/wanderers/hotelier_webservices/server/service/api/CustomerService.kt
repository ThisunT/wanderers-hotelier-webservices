package com.wanderers.hotelier_webservices.server.service.api

import com.wanderers.hotelier_webservices.server.exception.CustomerServiceException

interface CustomerService {

    /**
     * Check if the customer exist in the datasource
     *
     * @param customerId
     * @return
     * @throws CustomerServiceException
     */
    @kotlin.Throws(CustomerServiceException::class)
    fun isExistingCustomer(customerId: Int): Boolean
}