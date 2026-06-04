package com.library.service.loan.penalty;

import java.time.LocalDate;

// PATTERN: Strategy — algorithm for computing overdue penalties; swappable per item type
// PATTERN: Abstract Factory (product) — created as a family with LoanDurationPolicy by LoanPolicyFactory
public interface PenaltyStrategy {
    double calculate(LocalDate dueDate, LocalDate evaluationDate);
}
