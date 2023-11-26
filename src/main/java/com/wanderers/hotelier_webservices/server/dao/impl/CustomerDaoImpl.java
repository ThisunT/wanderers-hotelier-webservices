package com.wanderers.hotelier_webservices.server.dao.impl;

import com.wanderers.hotelier_webservices.server.dao.api.CustomerDao;
import com.wanderers.hotelier_webservices.server.exception.CustomerDaoException;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import static com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.EXISTS_CUSTOMER;

/**
 * Class is responsible for datasource manipulations of customer
 */
@Log4j2
@Repository("customer_dao")
public class CustomerDaoImpl implements CustomerDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    CustomerDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Boolean isExistingCustomer(int customerId) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", customerId);

            return namedParameterJdbcTemplate.queryForObject(EXISTS_CUSTOMER, params, Boolean.class);
        } catch (Exception e) {
            log.error("An error occurred while fetching customer", e);
            throw new CustomerDaoException("Failed to get the customer record", e);
        }
    }

}
