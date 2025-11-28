package com.example.laundry.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Student extends User and holds quota / dues and history.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Student extends User {
    private String rollNumber;
    private int quotaRemaining;        // number of washes remaining
    private BigDecimal amountDue;      // unpaid balance
    private String semester;
    private List<String> historyOrderIds = new ArrayList<>();

    public Student() {
        this.amountDue = BigDecimal.ZERO;
    }

    public Student(String id, String name, String email, String rollNumber, int quotaRemaining, String semester) {
        super(id, name, email, "STUDENT");
        this.rollNumber = rollNumber;
        this.quotaRemaining = quotaRemaining;
        this.semester = semester;
        this.amountDue = BigDecimal.ZERO;
    }

    // business helpers
    public void useQuota(int n) {
        this.quotaRemaining -= n;
    }

    public void addQuota(int n) {
        this.quotaRemaining += n;
    }

    public void charge(BigDecimal amount) {
        if (amount == null) return;
        if (this.amountDue == null) this.amountDue = BigDecimal.ZERO;
        this.amountDue = this.amountDue.add(amount);
    }

    public void pay(BigDecimal amount) {
        if (amount == null) return;
        if (this.amountDue == null) this.amountDue = BigDecimal.ZERO;
        this.amountDue = this.amountDue.subtract(amount);
        if (this.amountDue.signum() < 0) this.amountDue = BigDecimal.ZERO;
    }

    // getters / setters
    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public int getQuotaRemaining() { return quotaRemaining; }
    public void setQuotaRemaining(int quotaRemaining) { this.quotaRemaining = quotaRemaining; }

    public BigDecimal getAmountDue() { return amountDue; }
    public void setAmountDue(BigDecimal amountDue) { this.amountDue = amountDue; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public List<String> getHistoryOrderIds() { return historyOrderIds; }
    public void setHistoryOrderIds(List<String> historyOrderIds) { this.historyOrderIds = historyOrderIds; }
}