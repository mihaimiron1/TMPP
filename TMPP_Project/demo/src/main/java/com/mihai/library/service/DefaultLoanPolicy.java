package com.mihai.library.service;

import java.time.LocalDate;

import com.mihai.library.domain.LibraryItem;

public final class DefaultLoanPolicy implements LoanPolicy {

    @Override
    public LocalDate computeDueDate(LibraryItem item, LocalDate loanDate) {
        // minim: aceeași regulă pentru toate
        // (mai târziu poți face diferit pe BOOK/MAGAZINE/DVD fără să schimbi service-ul)
        return loanDate.plusDays(14);
    }
}