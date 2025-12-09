package com.example.laundry.service;

import com.example.laundry.config.PricingConfig;
import com.example.laundry.model.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Pricing rules:
 * - If WASH requested and student has quota & useQuota==true -> wash free
 * (quota consumed at approval).
 * - If no quota -> fixed wash fee (WASH_FEE_NO_QUOTA).
 * - DRY fee always DRY_FEE (configurable).
 * - Ironing computed from order items.
 *
 * Values injected from PricingConfig.
 */
@Service
public class PricingService {

    private final PricingConfig config;

    public PricingService(PricingConfig config) {
        this.config = config;
    }

    public BigDecimal computeTotalEstimate(Student student, Set<ServiceType> services, boolean useQuota,
            Double weightKg, List<OrderItem> itemsToIron) {
        BigDecimal total = BigDecimal.ZERO;

        // Wash
        // Wash
        if (services != null && services.contains(ServiceType.WASH)) {
            // Mandatory quota usage: If student has quota, use it (price 0).
            // Only charge if quota is exhausted or student unknown.
            if (student != null && student.getQuotaRemaining() > 0) {
                // free under quota
            } else {
                total = total.add(config.WASH_FEE_NO_QUOTA);
            }
        }

        // Dry
        if (services != null && services.contains(ServiceType.DRY)) {
            total = total.add(config.DRY_FEE);
        }

        // Ironing
        if (services != null && services.contains(ServiceType.IRON) && itemsToIron != null) {
            for (OrderItem it : itemsToIron) {
                total = total.add(it.totalIronCost());
            }
        }

        return total;
    }

    public BigDecimal getIronCostForType(String type) {
        if (type == null)
            return BigDecimal.ZERO;
        return config.DEFAULT_IRON_COSTS.getOrDefault(type.toUpperCase(), BigDecimal.ZERO);
    }
}
