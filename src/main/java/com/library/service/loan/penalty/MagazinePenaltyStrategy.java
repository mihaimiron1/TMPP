package com.library.service.loan.penalty;

import com.library.domain.model.Magazine;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// PATTERN: Strategy (concrete) — 0.5 MDL/day for magazines
public class MagazinePenaltyStrategy implements PenaltyStrategy {

    @Override
    public double calculate(LocalDate dueDate, LocalDate evaluationDate) {
        if (!evaluationDate.isAfter(dueDate)) return 0.0;
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, evaluationDate);
        return daysOverdue * Magazine.DEFAULT_PENALTY_MDL;
    }
}
