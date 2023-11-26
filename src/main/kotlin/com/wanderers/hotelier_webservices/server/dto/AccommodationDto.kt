package com.wanderers.hotelier_webservices.server.dto

import com.wanderers.hotelier_webservices.rest.model.ReputationBadgeEnum
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

@Getter
@Setter
@NoArgsConstructor
class AccommodationDto {
    private val id = 0
    private val hotelierId: String? = null
    private val name: String? = null
    private val rating = 0
    private val category: String? = null
    private val city: String? = null
    private val state: String? = null
    private val country: String? = null
    private val zipCode: String? = null
    private val address: String? = null
    private val image: String? = null
    private val reputation = 0
    private val reputationBadge: ReputationBadgeEnum? = null
    private val price = 0
    private val availability = 0
}