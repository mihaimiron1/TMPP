package com.mihai.library.service;

import java.time.LocalDate;

import com.mihai.library.domain.LibraryItem;

public interface LoanPolicy {
    LocalDate computeDueDate(LibraryItem item, LocalDate loanDate);
}