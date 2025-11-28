package com.example.laundry.util;

import com.example.laundry.model.ServiceType;

import java.util.Set;

/**
 * Validation helpers used by OrderService and controllers.
 */
public final class ValidationUtils {

    private ValidationUtils() {}

    /**
     * Valid wash/dry combinations + iron rules.
     */
    public static void validateServices(Set<ServiceType> services) {
        if (services == null || services.isEmpty()) {
            throw new IllegalArgumentException("At least one service must be selected");
        }

        // Iron with no items is fine — handled later — but services must be valid enum values.
        for (ServiceType s : services) {
            if (s == null) throw new IllegalArgumentException("Null service type not allowed");
        }
    }

    /**
     * Validate weight limit for wash/dry services.
     */
    public static void validateWeight(Double weightKg) {
        if (weightKg == null) {
            throw new IllegalArgumentException("Weight must be provided for WASH or DRY service.");
        }
        if (weightKg <= 0)
            throw new IllegalArgumentException("Weight must be greater than 0");

        if (weightKg > 8)
            throw new IllegalArgumentException("Weight exceeds max limit (8 kg). Split into multiple orders.");
    }
}
