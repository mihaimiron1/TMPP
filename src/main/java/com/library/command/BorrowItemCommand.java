package com.library.command;

import com.library.domain.enums.ItemType;
import com.library.domain.model.Loan;
import com.library.service.loan.LoanService;

// PATTERN: Command (concrete) — encapsulates a borrow operation; invoker does not know its internals
public class BorrowItemCommand implements LibraryCommand<Loan> {

    private final LoanService loanService;
    private final String      userId;
    private final String      itemId;
    private final ItemType    itemType;

    public BorrowItemCommand(LoanService loanService, String userId,
                             String itemId, ItemType itemType) {
        this.loanService = loanService;
        this.userId      = userId;
        this.itemId      = itemId;
        this.itemType    = itemType;
    }

    @Override
    public Loan execute() {
        return loanService.borrowItem(userId, itemId, itemType);
    }
}
