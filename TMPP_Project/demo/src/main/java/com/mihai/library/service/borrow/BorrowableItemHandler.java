package com.mihai.library.service.borrow;

import com.mihai.library.domain.LibraryItemGroup;

public final class BorrowableItemHandler extends BaseBorrowHandler {
    @Override
    public void handle(BorrowRequestContext context) {
        if (context == null) {
            throw new IllegalArgumentException("context null");
        }
        if (context.getItem() == null) {
            throw new IllegalArgumentException("item missing from context");
        }
        if (context.getItem() instanceof LibraryItemGroup) {
            throw new IllegalArgumentException("Composite items cannot be borrowed directly: " + context.getItem().getId());
        }
        next(context);
    }
}
