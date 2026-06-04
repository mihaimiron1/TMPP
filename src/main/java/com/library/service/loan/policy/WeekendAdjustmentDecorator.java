package com.library.service.loan.policy;

import java.time.LocalDate;

// PATTERN: Decorator (concrete) — moves a due date that lands on Saturday or Sunday to the following Monday
public class WeekendAdjustmentDecorator extends LoanPolicyDecorator {

    public WeekendAdjustmentDecorator(LoanDurationPolicy wrapped) {
        super(wrapped);
    }

    @Override
    public LocalDate calculateDueDate(LocalDate borrowDate) {
        LocalDate dueDate = wrapped.calculateDueDate(borrowDate);
        return switch (dueDate.getDayOfWeek()) {
            case SATURDAY -> dueDate.plusDays(2);
            case SUNDAY   -> dueDate.plusDays(1);
            default       -> dueDate;
        };
    }
}
