package com.library.observer;

import com.library.domain.model.Loan;
import org.springframework.stereotype.Component;

// PATTERN: Observer (concrete) — logs every loan event to the console for audit purposes
@Component
public class AuditLogObserver implements LoanEventObserver {

    @Override
    public void onLoanBorrowed(Loan loan) {
        System.out.printf("[AUDIT] BORROWED  loanId=%s userId=%s itemId=%s due=%s%n",
            loan.getId(), loan.getUserId(), loan.getItemId(), loan.getDueDate());
    }

    @Override
    public void onLoanReturned(Loan loan) {
        System.out.printf("[AUDIT] RETURNED  loanId=%s penalty=%.2f MDL%n",
            loan.getId(), loan.getPenaltyMDL());
    }

    @Override
    public void onLoanOverdue(Loan loan) {
        System.out.printf("[AUDIT] OVERDUE   loanId=%s userId=%s dueDate=%s%n",
            loan.getId(), loan.getUserId(), loan.getDueDate());
    }
}
