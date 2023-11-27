package com.wanderers.hotelier_webservices.server.service.impl

import com.wanderers.hotelier_webservices.server.dao.api.CustomerDao
import com.wanderers.hotelier_webservices.server.exception.CustomerServiceException
import com.wanderers.hotelier_webservices.server.service.api.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("customer_service")
class CustomerServiceImpl @Autowired internal constructor(customerDao: CustomerDao) : CustomerService {
    private val customerDao: CustomerDao

    init {
        this.customerDao = customerDao
    }

    @kotlin.Throws(CustomerServiceException::class)
    override fun isExistingCustomer(customerId: Int): Boolean {
        return try {
            customerDao.isExistingCustomer(customerId)
        } catch (e: Exception) {
            throw CustomerServiceException("Failed retrieving customer by id", e)
        }
    }
}