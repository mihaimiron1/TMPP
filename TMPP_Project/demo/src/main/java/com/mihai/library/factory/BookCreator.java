package com.mihai.library.factory;

import com.mihai.library.domain.Book;
import com.mihai.library.domain.LibraryItem;

public final class BookCreator extends LibraryItemCreator {
    @Override
    protected LibraryItem createItem(ItemRequest r) {
        if (r.getAuthor() == null || r.getAuthor().isBlank())
            throw new IllegalArgumentException("author invalid");
        if (r.getIsbn() == null || r.getIsbn().isBlank())
            throw new IllegalArgumentException("isbn invalid");
        return new Book(r.getId(), r.getTitle(), r.getAuthor(), r.getIsbn());
    }
}
