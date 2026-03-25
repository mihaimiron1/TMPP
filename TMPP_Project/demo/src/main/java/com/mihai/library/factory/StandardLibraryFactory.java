package com.mihai.library.factory;

import com.mihai.library.service.DefaultLoanPolicy;
import com.mihai.library.service.LoanPolicy;
import com.mihai.library.service.decorator.ItemTypeLoanPolicyDecorator;
import com.mihai.library.service.decorator.WeekendAdjustmentLoanPolicyDecorator;

public final class StandardLibraryFactory implements LibraryAbstractFactory {

    @Override
    public LibraryItemCreator bookCreator() {
        return new BookCreator();
    }

    @Override
    public LibraryItemCreator magazineCreator() {
        return new MagazineCreator();
    }

    @Override
    public LibraryItemCreator dvdCreator() {
        return new DvdCreator();
    }

    @Override
    public LibraryItemCreator groupCreator() {
        return new GroupCreator();
    }

    @Override
    public LoanPolicy loanPolicy() {
        LoanPolicy basePolicy = new DefaultLoanPolicy();
        LoanPolicy itemAwarePolicy = new ItemTypeLoanPolicyDecorator(basePolicy);
        return new WeekendAdjustmentLoanPolicyDecorator(itemAwarePolicy);
    } // 14 zile (cum ai)
}