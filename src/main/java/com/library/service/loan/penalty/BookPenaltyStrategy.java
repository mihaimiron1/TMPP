package com.library.service.loan.penalty;

import com.library.domain.model.Book;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// PATTERN: Strategy (concrete) — 1.5 MDL/day for books
public class BookPenaltyStrategy implements PenaltyStrategy {

    @Override
    public double calculate(LocalDate dueDate, LocalDate evaluationDate) {
        if (!evaluationDate.isAfter(dueDate)) return 0.0;
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, evaluationDate);
        return daysOverdue * Book.DEFAULT_PENALTY_MDL;
    }
}
