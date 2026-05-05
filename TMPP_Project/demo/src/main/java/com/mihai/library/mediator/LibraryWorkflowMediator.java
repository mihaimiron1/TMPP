package com.mihai.library.mediator;

import com.mihai.library.domain.Loan;
import com.mihai.library.domain.ReturnReceipt;
import com.mihai.library.memento.BorrowCartService;
import com.mihai.library.service.LibraryService;
import com.mihai.library.service.penalty.PenaltyService;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        LocalDate evaluationDate = LocalDate.now();
        BigDecimal penalty = penaltyService.calculatePenaltyForActiveLoan(itemId, evaluationDate);
        Loan returnedLoan = libraryService.returnItem(itemId);
        return new ReturnReceipt(returnedLoan, penalty, evaluationDate);
    }

    @Override
    public List<Loan> checkoutBorrowCart(String memberId) {
        List<String> itemIds = borrowCartService.getCartItems(memberId);
        if (itemIds.isEmpty()) {
            return List.of();
        }

        List<Loan> loans = itemIds.stream()
                .map(itemId -> libraryService.borrowItem(memberId, itemId))
                .toList();
        borrowCartService.resetCart(memberId);
        return loans;
    }
}
