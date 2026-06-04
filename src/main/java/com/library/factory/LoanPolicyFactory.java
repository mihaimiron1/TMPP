package com.library.factory;

import com.library.service.loan.penalty.PenaltyStrategy;
import com.library.service.loan.policy.LoanDurationPolicy;

// PATTERN: Abstract Factory — creates a matched family of LoanDurationPolicy + PenaltyStrategy
//          so that duration and penalty rules are always consistent for a given item type
public interface LoanPolicyFactory {
    LoanDurationPolicy createDurationPolicy();
    PenaltyStrategy createPenaltyStrategy();
}
