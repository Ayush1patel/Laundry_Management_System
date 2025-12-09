package com.example.laundry.controller;

import com.example.laundry.dto.AddStudentRequest;
import com.example.laundry.dto.ApproveOrderRequest;
import com.example.laundry.model.LaundryOrder;
import com.example.laundry.model.Student;
import com.example.laundry.service.OrderService;
import com.example.laundry.service.StorageService;
import com.example.laundry.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Endpoints used by laundry staff for approvals and listing unapproved orders.
 */
@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final OrderService orderService;
    private final StorageService storage;
    private final StudentService studentService;

    public StaffController(OrderService orderService, StorageService storage, StudentService studentService) {
        this.orderService = orderService;
        this.storage = storage;
        this.studentService = studentService;
    }

    /**
     * List all unapproved orders.
     */
    @GetMapping("/unapproved")
    public ResponseEntity<?> listUnapproved() {
        try {
            List<LaundryOrder> list = storage.listUnapprovedOrders();
            return ResponseEntity.ok(list);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * Search unapproved by roll number.
     */
    @GetMapping("/unapproved/search")
    public ResponseEntity<?> searchUnapproved(@RequestParam String roll) {
        try {
            List<LaundryOrder> filtered = storage.listUnapprovedOrders().stream()
                    .filter(o -> roll.equals(o.getRollNumber()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filtered);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * Approve an unapproved order (collect payment outside of API or include
     * collectedAmount).
     */
    @PostMapping("/approve")
    public ResponseEntity<?> approveOrder(@RequestBody ApproveOrderRequest req) {
        try {
            if (req.getOrderId() == null || req.getOrderId().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "orderId required"));
            }
            orderService.approveOrder(req.getOrderId(), req.getCollectedAmount(), req.getApprovedBy());
            return ResponseEntity
                    .ok(Map.of("message", "Order approved and moved to queue", "orderId", req.getOrderId()));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * List queued orders (approved).
     */
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
     * Add a new student.
     */
    @PostMapping("/student")
    public ResponseEntity<?> addStudent(@RequestBody AddStudentRequest req) {
        try {
            if (req.getName() == null || req.getRollNumber() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Name and Roll Number are required"));
            }

            // check if roll already exists
            if (studentService.findByRoll(req.getRollNumber()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Student with this roll number already exists"));
            }

            // id format: USR-<ROLL> (or random UUID, but consistent ID is nice)
            String id = "USR-" + req.getRollNumber();
            Student s = new Student(id, req.getName(), req.getEmail(), req.getRollNumber(), req.getQuotaRemaining(),
                    req.getSemester());

            studentService.save(s);

            return ResponseEntity.ok(Map.of("message", "Student added", "studentId", id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }
}
