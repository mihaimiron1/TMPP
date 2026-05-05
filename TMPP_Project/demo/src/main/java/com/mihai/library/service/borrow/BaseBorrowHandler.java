package com.mihai.library.service.borrow;

public abstract class BaseBorrowHandler implements BorrowHandler {
    private BorrowHandler next;

    @Override
    public BorrowHandler setNext(BorrowHandler next) {
        this.next = next;
        return next;
    }

    protected final void next(BorrowRequestContext context) {
        if (next != null) {
            next.handle(context);
        }
    }
}
