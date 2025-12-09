package com.example.laundry.controller;

import com.example.laundry.dto.CreateOrderRequest;
import com.example.laundry.dto.CreateOrderResponse;
import com.example.laundry.model.LaundryOrder;
import com.example.laundry.model.Student;
import com.example.laundry.service.OrderService;
import com.example.laundry.service.StorageService;
import com.example.laundry.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Endpoints used by students.
 */
@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;
    private final OrderService orderService;
    private final StorageService storage;

    public StudentController(StudentService studentService, OrderService orderService, StorageService storage) {
        this.studentService = studentService;
        this.orderService = orderService;
        this.storage = storage;
    }

    /**
     * Create an order (goes to unapproved_orders.json).
     */
    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest req) {
        try {
            // find student if provided
            Student s = null;
            if (req.getStudentId() != null) {
                s = studentService.findById(req.getStudentId()).orElse(null);
            } else if (req.getRollNumber() != null) {
                s = studentService.findByRoll(req.getRollNumber()).orElse(null);
            }

            LaundryOrder o = orderService.createOrder(
                    s == null ? null : s.getId(),
                    req.getRollNumber(),
                    req.getServicesRequested(),
                    req.getWeightKg(),
                    req.getItemsToIron(),
                    req.isUseQuota());

            // The actual quota is NOT decremented here, only on approval.
            // For a better user experience, the response will reflect what the quota WILL
            // BE.
            int currentQuota = s == null ? 0 : s.getQuotaRemaining();
            int projectedQuota = currentQuota;

            boolean willUseQuota = req.getServicesRequested().contains(com.example.laundry.model.ServiceType.WASH)
                    && s != null && s.getQuotaRemaining() > 0;

            if (willUseQuota) {
                projectedQuota = currentQuota - 1;
            }

            CreateOrderResponse resp = new CreateOrderResponse(o.getId(), o.getEstimatedCost(), projectedQuota,
                    "Order created (UNAPPROVED)");
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create order", "details", ex.getMessage()));
        }
    }

    /**
     * Get quota remaining for a student id.
     */
    @GetMapping("/{studentId}/quota")
    public ResponseEntity<?> getQuota(@PathVariable String studentId) {
        try {
            Optional<Student> maybe = studentService.findById(studentId);
            if (maybe.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Student not found"));
            return ResponseEntity.ok(
                    Map.of("quotaRemaining", maybe.get().getQuotaRemaining(), "amountDue", maybe.get().getAmountDue()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * Get all orders for a student (unapproved, queued, completed).
     * Query by studentId or roll (prefer studentId if both provided).
     */
    @GetMapping("/{studentId}/orders")
    public ResponseEntity<?> getStudentOrders(@PathVariable String studentId,
            @RequestParam(required = false) String roll) {
        try {
            String rollToMatch = roll;
            if (rollToMatch == null) {
                // try to find roll from studentId
                Optional<Student> maybe = studentService.findById(studentId);
                if (maybe.isPresent())
                    rollToMatch = maybe.get().getRollNumber();
            }

            final String finalRoll = rollToMatch;

            List<LaundryOrder> unapproved = storage.listUnapprovedOrders();
            List<LaundryOrder> queued = storage.listQueuedOrders();
            List<LaundryOrder> completed = storage.listCompletedOrders();

            // combine and filter by studentId or rollNumber
            List<LaundryOrder> combined = new ArrayList<>();
            combined.addAll(unapproved);
            combined.addAll(queued);
            combined.addAll(completed);

            List<LaundryOrder> filtered = combined.stream()
                    .filter(o -> (studentId != null && studentId.equals(o.getStudentId()))
                            || (finalRoll != null && finalRoll.equals(o.getRollNumber())))
                    .sorted(Comparator.comparing(LaundryOrder::getCreatedAt).reversed())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(filtered);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }
}
