package com.example.laundry.dto;

import com.example.laundry.model.OrderItem;
import com.example.laundry.model.ServiceType;

import java.util.List;
import java.util.Set;

/**
 * Incoming DTO used when a student creates an order.
 */
public class CreateOrderRequest {

    private String studentId;
    private String rollNumber;
    private Set<ServiceType> servicesRequested;
    private Double weightKg;
    private List<OrderItem> itemsToIron;
    private boolean useQuota = true;

    public CreateOrderRequest() {}

    // getters / setters
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

    public boolean isUseQuota() { return useQuota; }
    public void setUseQuota(boolean useQuota) { this.useQuota = useQuota; }
}