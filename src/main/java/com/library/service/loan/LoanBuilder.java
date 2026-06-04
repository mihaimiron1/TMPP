package com.library.service.loan;

import com.library.domain.enums.ItemType;
import com.library.domain.enums.LoanStatus;
import com.library.domain.model.Loan;
import com.library.service.loan.policy.LoanDurationPolicy;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

// PATTERN: Builder — constructs a Loan step by step; applyPolicy() integrates Abstract Factory output
public class LoanBuilder {

    private String id           = UUID.randomUUID().toString();
    private String userId;
    private String itemId;
    private ItemType itemType;
    private LocalDate borrowDate = LocalDate.now();
    private LocalDate dueDate;
    private LoanStatus status    = LoanStatus.ACTIVE;
    private double penaltyMDL    = 0.0;

    public LoanBuilder userId(String userId) {
        this.userId = userId;
        return this;
    }

    public LoanBuilder itemId(String itemId) {
        this.itemId = itemId;
        return this;
    }

    public LoanBuilder itemType(ItemType itemType) {
        this.itemType = itemType;
        return this;
    }

    public LoanBuilder borrowDate(LocalDate date) {
        this.borrowDate = date;
        return this;
    }

    public LoanBuilder dueDate(LocalDate date) {
        this.dueDate = date;
        return this;
    }

    // Computes dueDate from the policy returned by Abstract Factory
    public LoanBuilder applyPolicy(LoanDurationPolicy policy) {
        this.dueDate = policy.calculateDueDate(this.borrowDate);
        return this;
    }

    public LoanBuilder status(LoanStatus status) {
        this.status = status;
        return this;
    }

    public LoanBuilder penaltyMDL(double penalty) {
        this.penaltyMDL = penalty;
        return this;
    }

    public Loan build() {
        Objects.requireNonNull(userId,   "userId is required");
        Objects.requireNonNull(itemId,   "itemId is required");
        Objects.requireNonNull(itemType, "itemType is required");
        Objects.requireNonNull(dueDate,  "dueDate is required — call applyPolicy() or dueDate()");
        Loan loan = new Loan(id, userId, itemId, itemType, borrowDate, dueDate, status);
        loan.setPenaltyMDL(penaltyMDL);
        return loan;
    }
}
