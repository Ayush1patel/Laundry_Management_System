package com.example.laundry.controller;

import com.example.laundry.dto.PickupRequest;
import com.example.laundry.model.LaundryOrder;
import com.example.laundry.service.OrderService;
import com.example.laundry.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * General order endpoints: list unapproved/queue and pickup with OTP.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final StorageService storage;

    public OrderController(OrderService orderService, StorageService storage) {
        this.orderService = orderService;
        this.storage = storage;
    }

    @GetMapping("/unapproved")
    public ResponseEntity<?> listUnapproved() {
        try {
            List<LaundryOrder> list = storage.listUnapprovedOrders();
            return ResponseEntity.ok(list);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/queue")
    public ResponseEntity<?> listQueue() {
        try {
            List<LaundryOrder> list = storage.listQueuedOrders();
            return ResponseEntity.ok(list);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * Pickup endpoint - expects body { "otp": "1234" }
     */
    @PostMapping("/{orderId}/pickup")
    public ResponseEntity<?> pickup(@PathVariable String orderId, @RequestBody PickupRequest body) {
        try {
            String otp = body.getOtp();
            if (otp == null) return ResponseEntity.badRequest().body(Map.of("error", "otp required"));
            boolean ok = orderService.pickupWithOtp(orderId, otp);
            if (!ok) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "OTP invalid or expired"));
            return ResponseEntity.ok(Map.of("message", "Pickup recorded; order completed", "orderId", orderId));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * Get order by id searching unapproved, queued, then completed.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable String orderId) {
        try {
            Optional<LaundryOrder> u = storage.findUnapprovedById(orderId);
            if (u.isPresent()) return ResponseEntity.ok(u.get());
            Optional<LaundryOrder> q = storage.findQueuedById(orderId);
            if (q.isPresent()) return ResponseEntity.ok(q.get());
            List<LaundryOrder> completed = storage.listCompletedOrders();
            for (LaundryOrder o : completed) {
                if (orderId.equals(o.getId())) return ResponseEntity.ok(o);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Order not found"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }
}
