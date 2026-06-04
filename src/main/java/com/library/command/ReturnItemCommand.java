package com.library.command;

import com.library.domain.model.Loan;
import com.library.service.loan.LoanService;

// PATTERN: Command (concrete) — encapsulates a return operation
public class ReturnItemCommand implements LibraryCommand<Loan> {

    private final LoanService loanService;
    private final String      loanId;

    public ReturnItemCommand(LoanService loanService, String loanId) {
        this.loanService = loanService;
        this.loanId      = loanId;
    }

    @Override
    public Loan execute() {
        return loanService.returnItem(loanId);
    }
}
