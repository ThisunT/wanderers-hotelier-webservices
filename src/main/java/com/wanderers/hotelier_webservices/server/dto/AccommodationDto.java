package com.wanderers.hotelier_webservices.server.dto;

import com.wanderers.hotelier_webservices.rest.model.AccommodationResponseBody.ReputationBadgeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccommodationDto {
    private int id;
    private String hotelierId;
    private String name;
    private int rating;
    private String category;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String address;
    private String image;
    private int reputation;
    private ReputationBadgeEnum reputationBadge;
    private int price;
    private int availability;
}
