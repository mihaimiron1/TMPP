package com.library.visitor;

import com.library.domain.model.Book;
import com.library.domain.model.Dvd;
import com.library.domain.model.Magazine;

// PATTERN: Visitor — defines an operation that can be applied to each concrete item type
//          without modifying the item classes themselves
public interface LibraryItemVisitor<T> {
    T visit(Book book);
    T visit(Magazine magazine);
    T visit(Dvd dvd);
}
