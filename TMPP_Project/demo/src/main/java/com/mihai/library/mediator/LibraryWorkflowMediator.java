package com.mihai.library.mediator;

import com.mihai.library.domain.Loan;
import com.mihai.library.domain.ReturnReceipt;
import com.mihai.library.memento.BorrowCartService;
import com.mihai.library.service.LibraryService;
import com.mihai.library.service.penalty.PenaltyService;

import java.util.List;

public final class LibraryWorkflowMediator implements CirculationMediator {
    private final BorrowCartService borrowCartService;
    private final LibraryService libraryService;
    private final PenaltyService penaltyService;

    public LibraryWorkflowMediator(
            BorrowCartService borrowCartService,
            LibraryService libraryService,
            PenaltyService penaltyService) {
        if (borrowCartService == null) {
            throw new IllegalArgumentException("borrowCartService null");
        }
        if (libraryService == null) {
            throw new IllegalArgumentException("libraryService null");
        }
        if (penaltyService == null) {
            throw new IllegalArgumentException("penaltyService null");
        }
        this.borrowCartService = borrowCartService;
        this.libraryService = libraryService;
        this.penaltyService = penaltyService;
    }

    @Override
    public ReturnReceipt returnItemWithPenalty(String itemId) {
        return new ReturnItemWithPenaltyWorkflow(libraryService, penaltyService, itemId).execute();
    }

    @Override
    public List<Loan> checkoutBorrowCart(String memberId) {
        return new CheckoutBorrowCartWorkflow(borrowCartService, libraryService, memberId).execute();
    }
}
