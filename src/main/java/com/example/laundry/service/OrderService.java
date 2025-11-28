package com.example.laundry.service;

import com.example.laundry.model.*;
import com.example.laundry.util.IdUtils;
import com.example.laundry.util.ValidationUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Core business logic for creating orders, approving, and pickup.
 *
 * - createOrder(): validates and writes to unapproved_orders.json
 * - approveOrder(): moves from unapproved -> queue, consumes quota if applicable, sets ETA and OTP
 * - pickupWithOtp(): verifies OTP and archives order as completed
 */
@Service
public class OrderService {

    private final StorageService storage;
    private final StudentService studentService;
    private final PricingService pricing;
    private final OtpService otpService;
    private final EtaService etaService;

    public OrderService(StorageService storage,
                        StudentService studentService,
                        PricingService pricing,
                        OtpService otpService,
                        EtaService etaService) {
        this.storage = storage;
        this.studentService = studentService;
        this.pricing = pricing;
        this.otpService = otpService;
        this.etaService = etaService;
    }

    public LaundryOrder createOrder(String studentId, String rollNumber, Set<ServiceType> services,
                                    Double weightKg, List<OrderItem> itemsToIron, boolean useQuota) throws Exception {
        ValidationUtils.validateServices(services);
        if (services.contains(ServiceType.WASH) || services.contains(ServiceType.DRY)) {
            ValidationUtils.validateWeight(weightKg);
        }

        String id = IdUtils.newOrderId();
        LaundryOrder order = new LaundryOrder(id, studentId, rollNumber);
        order.setCreatedAt(LocalDateTime.now());
        order.setServicesRequested(services);
        order.setWeightKg(weightKg);
        if (itemsToIron != null) order.setItemsToIron(itemsToIron);
        order.setUseQuota(useQuota);

        Student student = null;
        if (studentId != null) student = studentService.findById(studentId).orElse(null);

        BigDecimal estimate = pricing.computeTotalEstimate(student, services, useQuota, weightKg, itemsToIron);
        order.setEstimatedCost(estimate);

        storage.saveUnapprovedOrder(order);
        return order;
    }

    public List<LaundryOrder> listUnapproved() throws Exception {
        return storage.listUnapprovedOrders();
    }

    public void approveOrder(String orderId, BigDecimal collectedAmount, String approvedBy) throws Exception {
        Optional<LaundryOrder> opt = storage.findUnapprovedById(orderId);
        if (opt.isEmpty()) throw new RuntimeException("Order not found in unapproved: " + orderId);
        LaundryOrder o = opt.get();

        // consume quota if applicable
        if (o.isUseQuota() && o.getServicesRequested().contains(ServiceType.WASH) && o.getStudentId() != null) {
            storage.findStudentById(o.getStudentId()).ifPresent(s -> {
                if (s.getQuotaRemaining() > 0) {
                    s.useQuota(1);
                    try {
                        storage.saveStudent(s);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }

        o.setStatus(OrderStatus.APPROVED);
        o.setApprovedAt(LocalDateTime.now());
        o.setEta(etaService.estimateEtaForNewOrder());
        Otp otp = otpService.generateOtpForOrder(o.getId(), 15);
        o.setOtp(otp);

        storage.saveQueuedOrder(o);
        storage.deleteUnapprovedById(o.getId());
    }

    public List<LaundryOrder> listQueue() throws Exception {
        return storage.listQueuedOrders();
    }

    public boolean pickupWithOtp(String orderId, String otpAttempt) throws Exception {
        Optional<LaundryOrder> opt = storage.findQueuedById(orderId);
        if (opt.isEmpty()) throw new RuntimeException("Order not found in queue: " + orderId);
        LaundryOrder order = opt.get();
        boolean ok = otpService.verifyOtp(orderId, otpAttempt);
        if (!ok) return false;
        order.setStatus(OrderStatus.PICKED_UP);
        // record completion time in eta field for analytics / ETAService
        LocalDateTime completedAt = LocalDateTime.now();
        order.setEta(completedAt);
        storage.saveCompletedOrder(order);
        // update ETA statistics
        etaService.recordCompletion(order, completedAt);
        return true;
    }
}
