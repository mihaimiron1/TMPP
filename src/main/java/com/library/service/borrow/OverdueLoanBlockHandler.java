package com.library.service.borrow;

import com.library.domain.enums.LoanStatus;
import com.library.domain.repository.LoanRepository;

import java.time.LocalDate;

// PATTERN: Chain of Responsibility (handler 5/5) — blocks new borrows if user has any overdue items
public class OverdueLoanBlockHandler extends BorrowHandler {

    private final LoanRepository loanRepo;

    public OverdueLoanBlockHandler(LoanRepository loanRepo) {
        this.loanRepo = loanRepo;
    }

    @Override
    public void handle(BorrowContext ctx) {
        LocalDate today = LocalDate.now();
        boolean hasOverdue = loanRepo.findByUserId(ctx.getUserId()).stream()
            .anyMatch(l -> l.getStatus() == LoanStatus.OVERDUE
                || (l.getStatus() == LoanStatus.ACTIVE && l.getDueDate().isBefore(today)));
        if (hasOverdue) {
            throw new RuntimeException(
                "Cannot borrow: please return your overdue items first");
        }
        passToNext(ctx);
    }
}
