package com.mihai.library.service.penalty;

import com.mihai.library.domain.LibraryItem;
import com.mihai.library.domain.Loan;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PenaltyStrategy {
    BigDecimal computePenalty(LibraryItem item, Loan loan, LocalDate evaluationDate);
}
