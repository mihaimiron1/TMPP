package com.library.service.loan.state;

import com.library.domain.enums.LoanStatus;

// PATTERN: State (concrete) — loan is active and within due date; both return and extend are allowed
public class ActiveLoanState implements LoanState {
    @Override public boolean canReturn()     { return true; }
    @Override public boolean canExtend()     { return true; }
    @Override public LoanStatus getStatus()  { return LoanStatus.ACTIVE; }
    @Override public String getStatusLabel() { return "Active"; }
}
