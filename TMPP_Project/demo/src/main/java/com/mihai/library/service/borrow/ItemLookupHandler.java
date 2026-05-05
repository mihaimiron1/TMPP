package com.mihai.library.service.borrow;

import com.mihai.library.repo.Catalog;
import com.mihai.library.service.exceptions.ItemNotFoundException;

public final class ItemLookupHandler extends BaseBorrowHandler {
    private final Catalog catalog;

    public ItemLookupHandler(Catalog catalog) {
        if (catalog == null) {
            throw new IllegalArgumentException("catalog null");
        }
        this.catalog = catalog;
    }

    @Override
    public void handle(BorrowRequestContext context) {
        if (context == null) {
            throw new IllegalArgumentException("context null");
        }

        context.setItem(catalog.findById(context.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("Item inexistent: " + context.getItemId())));
        next(context);
    }
}
