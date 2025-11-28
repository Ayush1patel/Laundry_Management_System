package com.example.laundry.service;

import com.example.laundry.model.LaundryOrder;
import com.example.laundry.model.Student;
import com.example.laundry.storage.JsonRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Simple JSON file based storage implementation.
 * - Keeps small in-memory cache for reads/writes.
 * - Persists lists atomically on change.
 *
 * Suitable for small dataset (campus laundry). For scale, swap to DB.
 */
@Service
public class JsonFileStorageService implements StorageService {

    private final Path dataDir;
    private final Path studentsFile;
    private final Path unapprovedFile;
    private final Path queueFile;
    private final Path completedFile;

    private final JsonRepository repo = new JsonRepository();

    private final Map<String, Student> students = new ConcurrentHashMap<>();
    private final Map<String, LaundryOrder> unapproved = new ConcurrentHashMap<>();
    private final Map<String, LaundryOrder> queued = new ConcurrentHashMap<>();
    private final Map<String, LaundryOrder> completed = new ConcurrentHashMap<>();

    public JsonFileStorageService(@Value("#{dataDir}") String dataDirStr) throws IOException {
        this.dataDir = Paths.get(dataDirStr);
        this.studentsFile = dataDir.resolve("students.json");
        this.unapprovedFile = dataDir.resolve("unapproved_orders.json");
        this.queueFile = dataDir.resolve("orders_queue.json");
        this.completedFile = dataDir.resolve("completed_orders.json");
        Files.createDirectories(this.dataDir);
        loadAll();
    }

    private void loadAll() throws IOException {
        repo.readList(studentsFile, Student.class).forEach(s -> students.put(s.getId(), s));
        repo.readList(unapprovedFile, LaundryOrder.class).forEach(o -> unapproved.put(o.getId(), o));
        repo.readList(queueFile, LaundryOrder.class).forEach(o -> queued.put(o.getId(), o));
        repo.readList(completedFile, LaundryOrder.class).forEach(o -> completed.put(o.getId(), o));
    }

    private synchronized void persistStudents() throws IOException {
        repo.writeList(studentsFile, new ArrayList<>(students.values()), Student.class);
    }

    private synchronized void persistUnapproved() throws IOException {
        repo.writeList(unapprovedFile, new ArrayList<>(unapproved.values()), LaundryOrder.class);
    }

    private synchronized void persistQueued() throws IOException {
        repo.writeList(queueFile, new ArrayList<>(queued.values()), LaundryOrder.class);
    }

    private synchronized void persistCompleted() throws IOException {
        repo.writeList(completedFile, new ArrayList<>(completed.values()), LaundryOrder.class);
    }

    // --- Students ---
    @Override
    public void saveStudent(Student s) throws Exception {
        students.put(s.getId(), s);
        persistStudents();
    }

    @Override
    public Optional<Student> findStudentById(String studentId) {
        return Optional.ofNullable(students.get(studentId));
    }

    @Override
    public Optional<Student> findStudentByRoll(String roll) {
        return students.values().stream().filter(s -> roll.equals(s.getRollNumber())).findFirst();
    }

    @Override
    public List<Student> listAllStudents() {
        return new ArrayList<>(students.values());
    }

    // --- Unapproved orders ---
    @Override
    public void saveUnapprovedOrder(LaundryOrder o) throws Exception {
        unapproved.put(o.getId(), o);
        persistUnapproved();
    }

    @Override
    public List<LaundryOrder> listUnapprovedOrders() {
        return unapproved.values().stream()
                .sorted(Comparator.comparing(LaundryOrder::getCreatedAt))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LaundryOrder> findUnapprovedById(String id) {
        return Optional.ofNullable(unapproved.get(id));
    }

    @Override
    public void deleteUnapprovedById(String id) throws Exception {
        unapproved.remove(id);
        persistUnapproved();
    }

    // --- Queue ---
    @Override
    public void saveQueuedOrder(LaundryOrder o) throws Exception {
        queued.put(o.getId(), o);
        persistQueued();
    }

    @Override
    public List<LaundryOrder> listQueuedOrders() {
        return queued.values().stream()
                .sorted(Comparator.comparing(LaundryOrder::getApprovedAt, Comparator.nullsFirst(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LaundryOrder> findQueuedById(String id) {
        return Optional.ofNullable(queued.get(id));
    }

    // --- Completed ---
    @Override
    public void saveCompletedOrder(LaundryOrder o) throws Exception {
        completed.put(o.getId(), o);
        // remove from queued if present
        queued.remove(o.getId());
        persistCompleted();
        persistQueued();
    }

    @Override
    public List<LaundryOrder> listCompletedOrders() {
        return completed.values().stream()
                .sorted(Comparator.comparing(LaundryOrder::getApprovedAt, Comparator.nullsFirst(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    @Override
    public Path getDataDir() {
        return dataDir;
    }
}
