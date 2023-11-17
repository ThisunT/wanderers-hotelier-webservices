package com.wanderers.hotelier_webservices.server.dto

import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

@Getter
@Setter
@NoArgsConstructor
class AccommodationDto {
    var id = 0
    var hotelierId: String? = null
    var name: String? = null
    var rating = 0
    var category: String? = null
    var city: String? = null
    var state: String? = null
    var country: String? = null
    var zipCode: String? = null
    var address: String? = null
    var image: String? = null
    var reputation = 0
    var reputationBadge: ReputationBadgeEnum? = null
    var price = 0
    var availability = 0
}