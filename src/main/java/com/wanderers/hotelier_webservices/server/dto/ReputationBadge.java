package com.wanderers.hotelier_webservices.server.dto;

public enum ReputationBadge {

    RED("red"),
    GREEN("green"),
    YELLOW("yellow");

    public final String value;

    ReputationBadge(String value) {
        this.value = value;
    }

    public static ReputationBadge fromValue(String value) {
        for (ReputationBadge b : ReputationBadge.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
