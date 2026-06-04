package com.library.service.loan.state;

import com.library.domain.enums.LoanStatus;

// PATTERN: State (concrete) — loan is closed; no further operations permitted
public class ReturnedLoanState implements LoanState {
    @Override public boolean canReturn()     { return false; }
    @Override public boolean canExtend()     { return false; }
    @Override public LoanStatus getStatus()  { return LoanStatus.RETURNED; }
    @Override public String getStatusLabel() { return "Returned"; }
}
