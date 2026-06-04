package com.library.service.loan.policy;

import java.time.LocalDate;

public class FixedDurationPolicy implements LoanDurationPolicy {

    private final int days;

    public FixedDurationPolicy(int days) {
        this.days = days;
    }

    @Override
    public LocalDate calculateDueDate(LocalDate borrowDate) {
        return borrowDate.plusDays(days);
    }

    public int getDays() { return days; }
}
