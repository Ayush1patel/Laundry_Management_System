package com.example.laundry.dto;

import java.math.BigDecimal;

/**
 * DTO used by staff to approve an unapproved order.
 */
public class ApproveOrderRequest {

    private String orderId;
    private BigDecimal collectedAmount;
    private String paymentMode; // CASH, UPI, CARD, etc.
    private String approvedBy;

    public ApproveOrderRequest() {}

    // getters / setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public BigDecimal getCollectedAmount() { return collectedAmount; }
    public void setCollectedAmount(BigDecimal collectedAmount) { this.collectedAmount = collectedAmount; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
}