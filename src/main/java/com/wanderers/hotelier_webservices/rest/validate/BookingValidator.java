package com.wanderers.hotelier_webservices.rest.validate;

import com.wanderers.hotelier_webservices.rest.exception.ExpiredTokenException;
import com.wanderers.hotelier_webservices.rest.exception.ResourceNotFoundException;
import com.wanderers.hotelier_webservices.rest.model.Booking;
import com.wanderers.hotelier_webservices.server.service.api.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("booking_validator")
public class BookingValidator {

    private final CustomerService customerService;

    @Autowired
    BookingValidator(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void validateBooking(Booking booking, String tokenStatus) {
        validateToken(tokenStatus);
        validateCustomer(booking.getCustomerId());
    }

    private void validateToken(String tokenStatus) {
        if(!Boolean.parseBoolean(tokenStatus)) {
            throw new ExpiredTokenException("Auth token is expired");
        }
    }

    private void validateCustomer(int customerId) {
        if (Boolean.FALSE.equals(customerService.isExistingCustomer(customerId))) {
            throw new ResourceNotFoundException("Customer: " + customerId + " does not exists in the system. Please register!");
        }
    }
}
