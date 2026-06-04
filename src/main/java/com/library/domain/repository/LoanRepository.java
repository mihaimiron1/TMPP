package com.library.domain.repository;

import com.library.domain.model.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanRepository {
    Loan save(Loan loan);
    Optional<Loan> findById(String id);
    List<Loan> findByUserId(String userId);
    List<Loan> findActiveByItemId(String itemId);
    List<Loan> findAllActive();
    void update(Loan loan);
}
