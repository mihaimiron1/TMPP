package com.library.service.loan.penalty;

import com.library.domain.model.Dvd;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// PATTERN: Strategy (concrete) — 2.0 MDL/day for DVDs
public class DvdPenaltyStrategy implements PenaltyStrategy {

    @Override
    public double calculate(LocalDate dueDate, LocalDate evaluationDate) {
        if (!evaluationDate.isAfter(dueDate)) return 0.0;
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, evaluationDate);
        return daysOverdue * Dvd.DEFAULT_PENALTY_MDL;
    }
}
