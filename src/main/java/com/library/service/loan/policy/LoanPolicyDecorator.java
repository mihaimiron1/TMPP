package com.library.service.loan.policy;

import java.time.LocalDate;

// PATTERN: Decorator (abstract) — wraps a LoanDurationPolicy to stack additional date adjustments
public abstract class LoanPolicyDecorator implements LoanDurationPolicy {

    protected final LoanDurationPolicy wrapped;

    protected LoanPolicyDecorator(LoanDurationPolicy wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public LocalDate calculateDueDate(LocalDate borrowDate) {
        return wrapped.calculateDueDate(borrowDate);
    }
}
