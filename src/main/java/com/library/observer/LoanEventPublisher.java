package com.library.observer;

import com.library.domain.model.Loan;
import org.springframework.stereotype.Component;

import java.util.List;

// PATTERN: Observer (publisher) — Spring injects all LoanEventObserver beans automatically
@Component
public class LoanEventPublisher {

    private final List<LoanEventObserver> observers;

    public LoanEventPublisher(List<LoanEventObserver> observers) {
        this.observers = observers;
    }

    public void publishBorrowed(Loan loan) {
        observers.forEach(o -> o.onLoanBorrowed(loan));
    }

    public void publishReturned(Loan loan) {
        observers.forEach(o -> o.onLoanReturned(loan));
    }

    public void publishOverdue(Loan loan) {
        observers.forEach(o -> o.onLoanOverdue(loan));
    }
}
