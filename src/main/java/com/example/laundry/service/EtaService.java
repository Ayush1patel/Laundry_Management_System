package com.example.laundry.service;

import com.example.laundry.model.LaundryOrder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Rolling-average ETA service. Keeps last N completion durations (minutes)
 * and returns a simple mean estimate. Also supports recording completions.
 */
@Service
public class EtaService {
    private final int window = 10;
    private final Deque<Long> lastDurations = new ArrayDeque<>();

    public synchronized void recordCompletion(LaundryOrder order, LocalDateTime readyAt) {
        if (order == null || order.getApprovedAt() == null || readyAt == null) return;
        long minutes = Duration.between(order.getApprovedAt(), readyAt).toMinutes();
        if (minutes < 0) return;
        lastDurations.addLast(minutes);
        if (lastDurations.size() > window) lastDurations.removeFirst();
    }

    public synchronized long estimateMinutesForNew() {
        if (lastDurations.isEmpty()) return 60; // fallback
        return (long) lastDurations.stream().mapToLong(Long::longValue).average().orElse(60);
    }

    public synchronized java.time.LocalDateTime estimateEtaForNewOrder() {
        return LocalDateTime.now().plusMinutes(estimateMinutesForNew());
    }

    /**
     * Bulk initialize durations from a list of completed orders (optional helper).
     */
    public synchronized void bulkRecordFromCompleted(List<LaundryOrder> completedOrders) {
        lastDurations.clear();
        if (completedOrders == null) return;
        for (LaundryOrder o : completedOrders) {
            if (o.getApprovedAt() != null && o.getEta() != null) {
                long minutes = Duration.between(o.getApprovedAt(), o.getEta()).toMinutes();
                lastDurations.addLast(minutes);
                if (lastDurations.size() > window) lastDurations.removeFirst();
            }
        }
    }
}
