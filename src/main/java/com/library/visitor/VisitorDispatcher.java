package com.library.visitor;

import com.library.domain.model.Book;
import com.library.domain.model.Dvd;
import com.library.domain.model.LibraryItem;
import com.library.domain.model.Magazine;

// PATTERN: Visitor — dispatcher replaces accept() on domain models, keeping the domain layer clean
public final class VisitorDispatcher {

    public static <T> T dispatch(LibraryItem item, LibraryItemVisitor<T> visitor) {
        return switch (item.getType()) {
            case BOOK     -> visitor.visit((Book) item);
            case MAGAZINE -> visitor.visit((Magazine) item);
            case DVD      -> visitor.visit((Dvd) item);
        };
    }

    private VisitorDispatcher() {}
}
