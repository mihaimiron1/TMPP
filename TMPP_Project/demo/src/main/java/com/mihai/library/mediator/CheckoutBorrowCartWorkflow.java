package com.mihai.library.mediator;

import com.mihai.library.domain.Loan;
import com.mihai.library.memento.BorrowCartService;
import com.mihai.library.service.LibraryService;

import java.util.List;

final class CheckoutBorrowCartWorkflow extends CirculationWorkflow<List<Loan>> {
    private final BorrowCartService borrowCartService;
    private final LibraryService libraryService;
    private final String memberId;
    private List<String> itemIds;

    CheckoutBorrowCartWorkflow(
            BorrowCartService borrowCartService,
            LibraryService libraryService,
            String memberId) {
        if (borrowCartService == null) {
            throw new IllegalArgumentException("borrowCartService null");
        }
        if (libraryService == null) {
            throw new IllegalArgumentException("libraryService null");
        }
        this.borrowCartService = borrowCartService;
        this.libraryService = libraryService;
        this.memberId = memberId;
        this.itemIds = List.of();
    }

    @Override
    protected void validate() {
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("memberId invalid");
        }
    }

    @Override
    protected void prepare() {
        itemIds = borrowCartService.getCartItems(memberId);
    }

    @Override
    protected List<Loan> process() {
        if (itemIds.isEmpty()) {
            return List.of();
        }

        return itemIds.stream()
                .map(itemId -> libraryService.borrowItem(memberId, itemId))
                .toList();
    }

    @Override
    protected void finish(List<Loan> result) {
        if (!itemIds.isEmpty()) {
            borrowCartService.resetCart(memberId);
        }
    }
}
