package com.library.service.item;

import com.library.domain.enums.ItemType;
import com.library.domain.model.LibraryItem;

import java.util.UUID;

// PATTERN: Factory Method — defines the createItem() hook; subclasses decide the concrete product type
public abstract class LibraryItemCreator {

    // PATTERN: Factory Method — the hook that subclasses override
    protected abstract LibraryItem createItem(ItemRequest request);

    // Template: validate → create → assign ID
    public final LibraryItem make(ItemRequest request) {
        validate(request);
        LibraryItem item = createItem(request);
        item.setId(UUID.randomUUID().toString());
        return item;
    }

    protected void validate(ItemRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }

    // Convenience factory: returns the right creator for a given type
    public static LibraryItemCreator forType(ItemType type) {
        return switch (type) {
            case BOOK     -> new BookCreator();
            case MAGAZINE -> new MagazineCreator();
            case DVD      -> new DvdCreator();
        };
    }
}
