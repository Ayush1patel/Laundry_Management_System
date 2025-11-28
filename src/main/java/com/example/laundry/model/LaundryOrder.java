package com.example.laundry.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Main order model. Holds services requested, weight, items for ironing,
 * estimated cost and status/OTP timestamps.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LaundryOrder {
    private String id;
    private String studentId;
    private String rollNumber; // convenience duplicate for searches
    private Set<ServiceType> servicesRequested = new HashSet<>();
    private Double weightKg;
    private List<OrderItem> itemsToIron = new ArrayList<>();
    private BigDecimal estimatedCost = BigDecimal.ZERO;
    private boolean useQuota = true;
    private OrderStatus status = OrderStatus.UNAPPROVED;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime approvedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime eta;

    private Otp otp;
    private String notes;

    public LaundryOrder() {
        this.createdAt = LocalDateTime.now();
    }

    public LaundryOrder(String id, String studentId, String rollNumber) {
        this.id = id;
        this.studentId = studentId;
        this.rollNumber = rollNumber;
        this.createdAt = LocalDateTime.now();
    }

    // --- cost helper ---
    public BigDecimal computeIroningCost() {
        BigDecimal sum = BigDecimal.ZERO;
        for (OrderItem it : itemsToIron) {
            sum = sum.add(it.totalIronCost());
        }
        return sum;
    }

    // --- getters / setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public Set<ServiceType> getServicesRequested() { return servicesRequested; }
    public void setServicesRequested(Set<ServiceType> servicesRequested) { this.servicesRequested = servicesRequested; }

    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }

    public List<OrderItem> getItemsToIron() { return itemsToIron; }
    public void setItemsToIron(List<OrderItem> itemsToIron) { this.itemsToIron = itemsToIron; }

    public BigDecimal getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(BigDecimal estimatedCost) { this.estimatedCost = estimatedCost; }

    public boolean isUseQuota() { return useQuota; }
    public void setUseQuota(boolean useQuota) { this.useQuota = useQuota; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }

    public LocalDateTime getEta() { return eta; }
    public void setEta(LocalDateTime eta) { this.eta = eta; }

    public Otp getOtp() { return otp; }
    public void setOtp(Otp otp) { this.otp = otp; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}