package com.mihai.library.service.borrow;

import com.mihai.library.repo.LoanRepository;
import com.mihai.library.service.exceptions.ItemAlreadyLoanedException;

public final class ItemAvailabilityHandler extends BaseBorrowHandler {
    private final LoanRepository loanRepository;

    public ItemAvailabilityHandler(LoanRepository loanRepository) {
        if (loanRepository == null) {
            throw new IllegalArgumentException("loanRepository null");
        }
        this.loanRepository = loanRepository;
    }

    @Override
    public void handle(BorrowRequestContext context) {
        if (context == null) {
            throw new IllegalArgumentException("context null");
        }

        loanRepository.findActiveLoanByItemId(context.getItemId()).ifPresent(loan -> {
            throw new ItemAlreadyLoanedException("Item deja imprumutat: " + context.getItemId());
        });
        next(context);
    }
}
