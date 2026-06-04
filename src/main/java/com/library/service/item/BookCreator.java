package com.library.service.item;

import com.library.domain.model.Book;
import com.library.domain.model.LibraryItem;

// PATTERN: Factory Method (concrete creator) — instantiates Book with book-specific fields
public class BookCreator extends LibraryItemCreator {

    @Override
    protected LibraryItem createItem(ItemRequest request) {
        double penalty = request.getDailyPenaltyMDL() > 0
            ? request.getDailyPenaltyMDL()
            : Book.DEFAULT_PENALTY_MDL;
        return new Book(
            null,
            request.getTitle(),
            request.getAuthor(),
            request.getIsbn(),
            request.getGenre(),
            request.getYear(),
            request.getQuantity(),
            request.getQuantity(),
            penalty
        );
    }

    @Override
    protected void validate(ItemRequest request) {
        super.validate(request);
        if (request.getAuthor() == null || request.getAuthor().isBlank()) {
            throw new IllegalArgumentException("Author is required for books");
        }
    }
}
