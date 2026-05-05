package com.mihai.library.service.borrow;

public final class ItemIdValidationHandler extends BaseBorrowHandler {
    @Override
    public void handle(BorrowRequestContext context) {
        if (context == null) {
            throw new IllegalArgumentException("context null");
        }
        if (context.getItemId() == null || context.getItemId().isBlank()) {
            throw new IllegalArgumentException("itemId invalid");
        }
        next(context);
    }
}
