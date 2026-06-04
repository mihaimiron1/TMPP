package com.library.service.loan.policy;

import java.time.LocalDate;

// PATTERN: Abstract Factory (product) — created as a family with PenaltyStrategy by LoanPolicyFactory
// PATTERN: Decorator — FixedDurationPolicy will be wrapped in Phase 5 (weekend adjustment)
public interface LoanDurationPolicy {
    LocalDate calculateDueDate(LocalDate borrowDate);
}
