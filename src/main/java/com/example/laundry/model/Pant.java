package com.example.laundry.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Pant item for ironing. Default ironing cost constant included.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pant extends Clothes {

    public static final BigDecimal DEFAULT_IRON_COST = BigDecimal.valueOf(20);

    public Pant() {
        super(UUID.randomUUID().toString(), 0.0, DEFAULT_IRON_COST);
    }

    public Pant(Double weightKg, BigDecimal ironCost) {
        super(
                UUID.randomUUID().toString(),
                weightKg,
                ironCost == null ? DEFAULT_IRON_COST : ironCost
        );
    }
}
