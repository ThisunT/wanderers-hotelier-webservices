package com.wanderers.hotelier_webservices.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookingDto {
    private int accommodationId;
    private int customerId;
}
