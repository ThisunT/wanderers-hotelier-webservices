package com.wanderers.hotelier_webservices.server.dao;

import com.wanderers.hotelier_webservices.server.exception.HotelierDaoException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import static com.wanderers.hotelier_webservices.server.dao.constants.QueryConstants.EXISTS_CUSTOMER;

/**
 * Class is responsible for datasource manipulations of customer
 */
@Repository("customer_dao")
public class CustomerDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    CustomerDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @SneakyThrows
    public Boolean isExistingCustomer(int customerId) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("id", customerId);

            return namedParameterJdbcTemplate.queryForObject(EXISTS_CUSTOMER, params, Boolean.class);
        } catch (Exception e) {
            throw new HotelierDaoException("Failed get the customer record", e);
        }
    }

}
