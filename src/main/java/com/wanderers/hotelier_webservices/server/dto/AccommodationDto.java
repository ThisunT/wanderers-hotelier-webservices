package com.wanderers.hotelier_webservices.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccommodationDto {
    private Integer id;
    private String hotelierId;
    private String name;
    private Integer rating;
    private Category category;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String address;
    private String image;
    private Integer reputation;
    private ReputationBadge reputationBadge;
    private Integer price;
    private Integer availability;
}
