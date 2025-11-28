package com.example.laundry.service;

import com.example.laundry.model.LaundryOrder;
import com.example.laundry.model.Student;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * Storage façade for JSON-backed persistence. Implementations must
 * ensure writes are atomic and thread-safe.
 */
public interface StorageService {

    // Students
    void saveStudent(Student s) throws Exception;
    Optional<Student> findStudentById(String studentId) throws Exception;
    Optional<Student> findStudentByRoll(String roll) throws Exception;
    List<Student> listAllStudents() throws Exception;

    // Unapproved orders
    void saveUnapprovedOrder(LaundryOrder o) throws Exception;
    List<LaundryOrder> listUnapprovedOrders() throws Exception;
    Optional<LaundryOrder> findUnapprovedById(String id) throws Exception;
    void deleteUnapprovedById(String id) throws Exception;

    // Queue / approved orders
    void saveQueuedOrder(LaundryOrder o) throws Exception;
    List<LaundryOrder> listQueuedOrders() throws Exception;
    Optional<LaundryOrder> findQueuedById(String id) throws Exception;

    // Completed orders (archive)
    void saveCompletedOrder(LaundryOrder o) throws Exception;
    List<LaundryOrder> listCompletedOrders() throws Exception;

    // Utility
    Path getDataDir();
}
