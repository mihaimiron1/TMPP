package com.library.service.loan.state;

import com.library.domain.enums.LoanStatus;

// PATTERN: State — defines state-dependent behavior for a Loan; each state decides what operations are valid
public interface LoanState {
    boolean canReturn();
    boolean canExtend();
    LoanStatus getStatus();
    String getStatusLabel();
}
