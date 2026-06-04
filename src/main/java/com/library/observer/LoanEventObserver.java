package com.library.observer;

import com.library.domain.model.Loan;

// PATTERN: Observer — contract for all objects that react to loan lifecycle events
public interface LoanEventObserver {
    void onLoanBorrowed(Loan loan);
    void onLoanReturned(Loan loan);
    void onLoanOverdue(Loan loan);
}
