package com.example.laundry.service;

import com.example.laundry.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Thin façade over storage for student-related operations.
 */
@Service
public class StudentService {

    private final StorageService storage;

    public StudentService(StorageService storage) {
        this.storage = storage;
    }

    public Optional<Student> findById(String id) throws Exception {
        return storage.findStudentById(id);
    }

    public Optional<Student> findByRoll(String roll) throws Exception {
        return storage.findStudentByRoll(roll);
    }

    public void save(Student s) throws Exception {
        storage.saveStudent(s);
    }

    public List<Student> listAll() throws Exception {
        return storage.listAllStudents();
    }
}
