package com.library.visitor;

import com.library.domain.model.Book;
import com.library.domain.model.Dvd;
import com.library.domain.model.Magazine;

// PATTERN: Visitor (concrete) — returns the overdue penalty rate in MDL/day per item type
public class PenaltyRateVisitor implements LibraryItemVisitor<Double> {

    @Override
    public Double visit(Book book)     { return book.getDailyPenaltyMDL(); }

    @Override
    public Double visit(Magazine mag)  { return mag.getDailyPenaltyMDL(); }

    @Override
    public Double visit(Dvd dvd)       { return dvd.getDailyPenaltyMDL(); }
}
