package com.library.factory;

import com.library.domain.enums.ItemType;

// Selects the correct Abstract Factory for a given item type
public final class LoanPolicyFactoryProvider {

    public static LoanPolicyFactory getFactory(ItemType type) {
        return switch (type) {
            case BOOK     -> new BookLoanPolicyFactory();
            case MAGAZINE -> new MagazineLoanPolicyFactory();
            case DVD      -> new DvdLoanPolicyFactory();
        };
    }

    private LoanPolicyFactoryProvider() {}
}
