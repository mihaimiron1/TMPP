package com.library.visitor;

import com.library.domain.model.Book;
import com.library.domain.model.Dvd;
import com.library.domain.model.Magazine;

// PATTERN: Visitor (concrete) — returns the maximum loan duration in days per item type
public class LoanDurationVisitor implements LibraryItemVisitor<Integer> {

    @Override
    public Integer visit(Book book)       { return Book.DEFAULT_LOAN_DAYS; }

    @Override
    public Integer visit(Magazine mag)    { return Magazine.DEFAULT_LOAN_DAYS; }

    @Override
    public Integer visit(Dvd dvd)         { return Dvd.DEFAULT_LOAN_DAYS; }
}
