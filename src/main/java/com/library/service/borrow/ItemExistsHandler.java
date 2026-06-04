package com.library.service.borrow;

import com.library.domain.repository.CatalogRepository;

// PATTERN: Chain of Responsibility (handler 2/5) — loads the requested item from catalog into context
public class ItemExistsHandler extends BorrowHandler {

    private final CatalogRepository catalogRepo;

    public ItemExistsHandler(CatalogRepository catalogRepo) {
        this.catalogRepo = catalogRepo;
    }

    @Override
    public void handle(BorrowContext ctx) {
        catalogRepo.findById(ctx.getItemId(), ctx.getItemType())
            .ifPresentOrElse(
                ctx::setItem,
                () -> { throw new RuntimeException("Item not found: " + ctx.getItemId()); }
            );
        passToNext(ctx);
    }
}
