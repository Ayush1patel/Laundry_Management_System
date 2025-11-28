package com.example.laundry.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Shirt item for ironing.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Shirt extends Clothes {

    public static final BigDecimal DEFAULT_IRON_COST = BigDecimal.valueOf(10);

    public Shirt() {
        super(UUID.randomUUID().toString(), 0.0, DEFAULT_IRON_COST);
    }

    public Shirt(Double weightKg, BigDecimal ironCost) {
        super(
                UUID.randomUUID().toString(),
                weightKg,
                ironCost == null ? DEFAULT_IRON_COST : ironCost
        );
    }
}
