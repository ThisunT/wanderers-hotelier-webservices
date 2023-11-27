package com.wanderers.hotelier_webservices.server.dto

import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum

data class AccommodationDto(
    var id: Int?,
    var hotelierId: String,
    var name: String,
    var rating: Int,
    var category: String,
    var city: String,
    var state: String,
    var country: String,
    var zipCode: String,
    var address: String,
    var image: String,
    var reputation: Int,
    var reputationBadge: ReputationBadgeEnum?,
    var price: Int,
    var availability: Int
)