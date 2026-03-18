package com.mihai.library.service;

import com.mihai.library.domain.LibraryItem;
import com.mihai.library.domain.LibraryItemGroup;
import com.mihai.library.domain.Loan;
import com.mihai.library.repo.Catalog;
import com.mihai.library.repo.LoanRepository;
import com.mihai.library.service.exceptions.ItemAlreadyLoanedException;
import com.mihai.library.service.exceptions.ItemNotFoundException;
import com.mihai.library.service.exceptions.LoanNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class LibraryService {
    private final Catalog catalog;
    private final LoanRepository loanRepository;
    private final LoanPolicy loanPolicy;

    public LibraryService(Catalog catalog, LoanRepository loanRepository, LoanPolicy loanPolicy) {
        if (catalog == null) throw new IllegalArgumentException("catalog null");
        if (loanRepository == null) throw new IllegalArgumentException("loanRepository null");
        if (loanPolicy == null) throw new IllegalArgumentException("loanPolicy null");
        this.catalog = catalog;
        this.loanRepository = loanRepository;
        this.loanPolicy = loanPolicy;
    }

    public Loan borrowItem(String memberId, String itemId) {
        String validatedMemberId = requireValidId(memberId, "memberId");
        String validatedItemId = requireValidId(itemId, "itemId");

        LibraryItem item = findItemOrThrow(validatedItemId);
        ensureItemCanBeBorrowed(item);
        ensureItemIsAvailable(validatedItemId);

        LocalDate now = LocalDate.now();
        LocalDate due = loanPolicy.computeDueDate(item, now);

        Loan loan = new Loan(UUID.randomUUID().toString(), validatedMemberId, validatedItemId, now, due);
        loanRepository.save(loan);
        return loan;
    }

    public Loan returnItem(String itemId) {
        String validatedItemId = requireValidId(itemId, "itemId");

        Loan activeLoan = findActiveLoanOrThrow(validatedItemId);

        activeLoan.markReturned(LocalDate.now());
        loanRepository.save(activeLoan);
        return activeLoan;
    }

    public List<Loan> listLoansForMember(String memberId) {
        String validatedMemberId = requireValidId(memberId, "memberId");
        return loanRepository.findByMemberId(validatedMemberId);
    }

    private static String requireValidId(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " invalid");
        }
        return value;
    }

    private LibraryItem findItemOrThrow(String itemId) {
        return catalog.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item inexistent: " + itemId));
    }

    private void ensureItemIsAvailable(String itemId) {
        loanRepository.findActiveLoanByItemId(itemId).ifPresent(loan -> {
            throw new ItemAlreadyLoanedException("Item deja împrumutat: " + itemId);
        });
    }

    private void ensureItemCanBeBorrowed(LibraryItem item) {
        if (item instanceof LibraryItemGroup) {
            throw new IllegalArgumentException("Composite items cannot be borrowed directly: " + item.getId());
        }
    }

    private Loan findActiveLoanOrThrow(String itemId) {
        return loanRepository.findActiveLoanByItemId(itemId)
                .orElseThrow(() -> new LoanNotFoundException("Nu există împrumut activ pentru item: " + itemId));
    }
}