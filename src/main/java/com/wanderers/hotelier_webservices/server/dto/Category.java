package com.wanderers.hotelier_webservices.server.dto;

public enum Category {
    HOTEL("hotel"),
    ALTERNATIVE("alternative"),
    HOSTEL("hostel"),
    LODGE("lodge"),
    RESORT("resort"),
    GUEST_HOUSE("guest-house");

    public final String value;

    Category(String value) {
        this.value = value;
    }

    public static Category fromValue(String value) {
        for (Category b : Category.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
