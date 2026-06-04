package com.library.service.loan.state;

import com.library.domain.enums.LoanStatus;

// PATTERN: State (concrete) — past due date; penalty accumulates, extension not allowed
public class OverdueLoanState implements LoanState {
    @Override public boolean canReturn()     { return true; }
    @Override public boolean canExtend()     { return false; }
    @Override public LoanStatus getStatus()  { return LoanStatus.OVERDUE; }
    @Override public String getStatusLabel() { return "Overdue — return immediately"; }
}
