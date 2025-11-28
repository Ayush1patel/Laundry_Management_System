package com.example.laundry.model;

/**
 * Order lifecycle statuses.
 */
public enum OrderStatus {
    UNAPPROVED,
    APPROVED,
    IN_WASH,
    DRYING,
    READY,
    PICKED_UP,
    COMPLETED,
    CANCELLED
}