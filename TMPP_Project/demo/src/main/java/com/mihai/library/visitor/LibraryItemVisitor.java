package com.mihai.library.visitor;

import com.mihai.library.domain.Book;
import com.mihai.library.domain.Dvd;
import com.mihai.library.domain.LibraryItemGroup;
import com.mihai.library.domain.Magazine;

public interface LibraryItemVisitor<R> {
    R visitBook(Book book);

    R visitMagazine(Magazine magazine);

    R visitDvd(Dvd dvd);

    R visitGroup(LibraryItemGroup group);
}
