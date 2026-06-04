package com.library.service.loan.state;

import com.library.domain.enums.LoanStatus;

// PATTERN: State (concrete) — loan was extended once; second extension not allowed
public class ExtendedLoanState implements LoanState {
    @Override public boolean canReturn()     { return true; }
    @Override public boolean canExtend()     { return false; }
    @Override public LoanStatus getStatus()  { return LoanStatus.EXTENDED; }
    @Override public String getStatusLabel() { return "Extended"; }
}
