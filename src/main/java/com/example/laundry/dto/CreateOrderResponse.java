package com.example.laundry.dto;

import java.math.BigDecimal;

/**
 * Response DTO returned when order creation succeeds.
 */
public class CreateOrderResponse {

    private String orderId;
    private BigDecimal estimatedCost;
    private int quotaRemaining;
    private String message;

    public CreateOrderResponse() {}

    public CreateOrderResponse(String orderId, BigDecimal estimatedCost, int quotaRemaining, String message) {
        this.orderId = orderId;
        this.estimatedCost = estimatedCost;
        this.quotaRemaining = quotaRemaining;
        this.message = message;
    }

    // getters / setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public BigDecimal getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(BigDecimal estimatedCost) { this.estimatedCost = estimatedCost; }

    public int getQuotaRemaining() { return quotaRemaining; }
    public void setQuotaRemaining(int quotaRemaining) { this.quotaRemaining = quotaRemaining; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}