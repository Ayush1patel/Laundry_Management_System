package com.example.laundry.util;

import java.util.UUID;

/**
 * Generates unique identifiers for:
 *  - order IDs
 *  - student/user IDs
 */
public final class IdUtils {

    private IdUtils() {}

    public static String newOrderId() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String newUserId() {
        return "USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
