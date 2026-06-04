package com.mihai.library.visitor;

import com.mihai.library.domain.Book;
import com.mihai.library.domain.Dvd;
import com.mihai.library.domain.LibraryItemGroup;
import com.mihai.library.domain.Magazine;

public final class CatalogStatisticsVisitor implements LibraryItemVisitor<Void> {
    private int books;
    private int magazines;
    private int dvds;
    private int groups;

    @Override
    public Void visitBook(Book book) {
        books++;
        return null;
    }

    @Override
    public Void visitMagazine(Magazine magazine) {
        magazines++;
        return null;
    }

    @Override
    public Void visitDvd(Dvd dvd) {
        dvds++;
        return null;
    }

    @Override
    public Void visitGroup(LibraryItemGroup group) {
        groups++;
        return null;
    }

    public CatalogStatistics statistics() {
        return new CatalogStatistics(books, magazines, dvds, groups);
    }
}
