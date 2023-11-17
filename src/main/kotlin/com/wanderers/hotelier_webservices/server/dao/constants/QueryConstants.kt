package com.wanderers.hotelier_webservices.server.dao.constants

object QueryConstants {
    const val INSERT_ACCOMMODATION = "WITH acc AS ( " +
            "INSERT INTO accommodation (name, rating, category, image, reputation, reputation_badge, price, availability, hotelier_id) " +
            "VALUES (:name, :rating, :category::accommodation_category_enum, :image, :reputation, :reputationBadge::reputation_badge_enum, :price, :availability, :hotelierId) " +
            "RETURNING id, reputation_badge " +
            ") " +
            ", loc AS ( " +
            "INSERT INTO location (city, state, country, zip_code, address, accommodation_id) " +
            "SELECT :city, :state, :country, :zipCode, :address, id FROM acc " +
            ") " +
            "SELECT id FROM acc"
    const val EXISTS_HOTELIER = "SELECT EXISTS (SELECT 1 FROM hotelier WHERE id = :id)"
    const val GET_ACCOMMODATION_BY_ID =
        "SELECT * FROM accommodation acc INNER JOIN location loc ON acc.id = loc.accommodation_id where acc.id = :id"
    const val GET_HOTELIER_BY_ACC_ID = "SELECT hotelier_id FROM accommodation where id = :id"
    const val DELETE_ACCOMMODATION_BY_ID = "DELETE FROM accommodation where id = :id"
    const val EXISTS_CUSTOMER = "SELECT EXISTS (SELECT 1 FROM customer WHERE id = :id)"
    const val GET_AVAILABILITY_BY_ACC_ID = "SELECT availability FROM accommodation where id = :id"
    const val INSERT_BOOKING =
        "INSERT INTO booking (customer_id, accommodation_id) VALUES (:customerId, :accommodationId)"
    const val UPDATE_AVAILABILITY = "UPDATE accommodation SET availability = :newAvailability where id = :id"
}