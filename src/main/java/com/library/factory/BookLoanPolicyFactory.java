package com.library.factory;

import com.library.domain.model.Book;
import com.library.service.loan.penalty.BookPenaltyStrategy;
import com.library.service.loan.penalty.PenaltyStrategy;
import com.library.service.loan.policy.FixedDurationPolicy;
import com.library.service.loan.policy.LoanDurationPolicy;

// PATTERN: Abstract Factory (concrete) — 14-day loan + 1.5 MDL/day penalty for books
public class BookLoanPolicyFactory implements LoanPolicyFactory {

    @Override
    public LoanDurationPolicy createDurationPolicy() {
        return new FixedDurationPolicy(Book.DEFAULT_LOAN_DAYS);
    }

    @Override
    public PenaltyStrategy createPenaltyStrategy() {
        return new BookPenaltyStrategy();
    }
}
