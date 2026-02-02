package com.mihai.library.repo;

import java.util.List;
import java.util.Optional;

import com.mihai.library.domain.Loan;

public interface LoanRepository {
    void save(Loan loan);
    Optional<Loan> findById(String loanId);
    Optional<Loan> findActiveLoanByItemId(String itemId);
    List<Loan> findByMemberId(String memberId);
}