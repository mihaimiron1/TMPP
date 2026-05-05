package com.mihai.library.mediator;

import com.mihai.library.domain.Loan;
import com.mihai.library.domain.ReturnReceipt;
import com.mihai.library.service.LibraryService;
import com.mihai.library.service.penalty.PenaltyService;

import java.math.BigDecimal;
import java.time.LocalDate;

final class ReturnItemWithPenaltyWorkflow extends CirculationWorkflow<ReturnReceipt> {
    private final LibraryService libraryService;
    private final PenaltyService penaltyService;
    private final String itemId;
    private LocalDate evaluationDate;

    ReturnItemWithPenaltyWorkflow(
            LibraryService libraryService,
            PenaltyService penaltyService,
            String itemId) {
        if (libraryService == null) {
            throw new IllegalArgumentException("libraryService null");
        }
        if (penaltyService == null) {
            throw new IllegalArgumentException("penaltyService null");
        }
        this.libraryService = libraryService;
        this.penaltyService = penaltyService;
        this.itemId = itemId;
    }

    @Override
    protected void validate() {
        if (itemId == null || itemId.isBlank()) {
            throw new IllegalArgumentException("itemId invalid");
        }
    }

    @Override
    protected void prepare() {
        evaluationDate = LocalDate.now();
    }

    @Override
    protected ReturnReceipt process() {
        BigDecimal penalty = penaltyService.calculatePenaltyForActiveLoan(itemId, evaluationDate);
        Loan returnedLoan = libraryService.returnItem(itemId);
        return new ReturnReceipt(returnedLoan, penalty, evaluationDate);
    }
}
