package com.library.service.loan.state;

import com.library.domain.enums.LoanStatus;
import com.library.domain.model.Loan;

import java.time.LocalDate;

// Derives the effective LoanState from persisted status + real-time date check
public final class LoanStateResolver {

    // A stored ACTIVE/EXTENDED loan is effectively OVERDUE if its due date has passed
    public static LoanState resolve(Loan loan, LocalDate today) {
        if (loan.getStatus() != LoanStatus.RETURNED && loan.getDueDate().isBefore(today)) {
            return new OverdueLoanState();
        }
        return switch (loan.getStatus()) {
            case ACTIVE   -> new ActiveLoanState();
            case OVERDUE  -> new OverdueLoanState();
            case RETURNED -> new ReturnedLoanState();
            case EXTENDED -> new ExtendedLoanState();
        };
    }

    private LoanStateResolver() {}
}
