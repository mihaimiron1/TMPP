package com.mihai.library.factory;

import com.mihai.library.service.DefaultLoanPolicy;
import com.mihai.library.service.LoanPolicy;

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
        return new DefaultLoanPolicy();
    } // 14 zile (cum ai)
}