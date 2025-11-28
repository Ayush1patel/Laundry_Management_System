package com.example.laundry.config;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Pricing and quota configuration.
 * Simple component exposing default prices and quota values.
 * You can replace this with a @ConfigurationProperties implementation
 * or externalized values later if you want to change prices without code changes.
 *
 * Fields are public final for easy, direct access in services (keeps code simple
 * for the baseline project). If you prefer encapsulation, change to private + getters.
 */
@Component
public class PricingConfig {

    // Dry fee is always charged when DRY service is requested
    public final BigDecimal DRY_FEE = BigDecimal.valueOf(100);

    // Wash fee if student doesn't use quota or quota unavailable (default)
    public final BigDecimal WASH_FEE_NO_QUOTA = BigDecimal.valueOf(50);

    // Number of free washes allocated per semester
    public final int QUOTA_PER_SEM = 12;

    // Default per-item ironing costs (modifiable)
    public final Map<String, BigDecimal> DEFAULT_IRON_COSTS = Map.of(
            "SHIRT", BigDecimal.valueOf(10),
            "PANT", BigDecimal.valueOf(20)
    );

    public PricingConfig() { }
}
