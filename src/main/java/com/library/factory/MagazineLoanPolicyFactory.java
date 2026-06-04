package com.library.factory;

import com.library.domain.model.Magazine;
import com.library.service.loan.penalty.MagazinePenaltyStrategy;
import com.library.service.loan.penalty.PenaltyStrategy;
import com.library.service.loan.policy.FixedDurationPolicy;
import com.library.service.loan.policy.LoanDurationPolicy;

// PATTERN: Abstract Factory (concrete) — 7-day loan + 0.5 MDL/day penalty for magazines
public class MagazineLoanPolicyFactory implements LoanPolicyFactory {

    @Override
    public LoanDurationPolicy createDurationPolicy() {
        return new FixedDurationPolicy(Magazine.DEFAULT_LOAN_DAYS);
    }

    @Override
    public PenaltyStrategy createPenaltyStrategy() {
        return new MagazinePenaltyStrategy();
    }
}
