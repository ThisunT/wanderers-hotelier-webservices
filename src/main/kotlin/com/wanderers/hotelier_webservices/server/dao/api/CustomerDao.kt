package com.wanderers.hotelier_webservices.server.dao.api

import com.wanderers.hotelier_webservices.server.exception.CustomerDaoException

/**
 * Data access contract for the customer entity
 */
interface CustomerDao {

    /**
     * Check if the customer exists in the datasource
     *
     * @param customerId
     * @return
     * @throws CustomerDaoException
     */
    @kotlin.Throws(CustomerDaoException::class)
    fun isExistingCustomer(customerId: Int): Boolean
}