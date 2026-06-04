package com.library.factory;

import com.library.domain.model.Dvd;
import com.library.service.loan.penalty.DvdPenaltyStrategy;
import com.library.service.loan.penalty.PenaltyStrategy;
import com.library.service.loan.policy.FixedDurationPolicy;
import com.library.service.loan.policy.LoanDurationPolicy;

// PATTERN: Abstract Factory (concrete) — 5-day loan + 2.0 MDL/day penalty for DVDs
public class DvdLoanPolicyFactory implements LoanPolicyFactory {

    @Override
    public LoanDurationPolicy createDurationPolicy() {
        return new FixedDurationPolicy(Dvd.DEFAULT_LOAN_DAYS);
    }

    @Override
    public PenaltyStrategy createPenaltyStrategy() {
        return new DvdPenaltyStrategy();
    }
}
