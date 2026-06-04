package com.library.service.borrow;

import com.library.domain.enums.LoanStatus;
import com.library.domain.repository.LoanRepository;

// PATTERN: Chain of Responsibility (handler 4/5) — enforces the 3-active-loan cap per user
public class ActiveLoansLimitHandler extends BorrowHandler {

    public static final int MAX_ACTIVE_LOANS = 3;
    private final LoanRepository loanRepo;

    public ActiveLoansLimitHandler(LoanRepository loanRepo) {
        this.loanRepo = loanRepo;
    }

    @Override
    public void handle(BorrowContext ctx) {
        long active = loanRepo.findByUserId(ctx.getUserId()).stream()
            .filter(l -> l.getStatus() == LoanStatus.ACTIVE
                      || l.getStatus() == LoanStatus.EXTENDED)
            .count();
        if (active >= MAX_ACTIVE_LOANS) {
            throw new RuntimeException(
                "Loan limit reached: you can have at most " + MAX_ACTIVE_LOANS + " active loans");
        }
        passToNext(ctx);
    }
}
