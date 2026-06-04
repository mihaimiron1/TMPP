package com.library.service.borrow;

// PATTERN: Chain of Responsibility (handler 3/5) — rejects borrow if no copies are currently available
public class ItemAvailabilityHandler extends BorrowHandler {

    @Override
    public void handle(BorrowContext ctx) {
        if (!ctx.getItem().isAvailable()) {
            throw new RuntimeException(
                "\"" + ctx.getItem().getTitle() + "\" has no available copies");
        }
        passToNext(ctx);
    }
}
