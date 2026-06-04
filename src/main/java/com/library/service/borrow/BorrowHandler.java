package com.library.service.borrow;

// PATTERN: Chain of Responsibility — abstract handler; subclasses validate one aspect of a borrow request
//          and either throw to reject or call passToNext() to continue down the chain
public abstract class BorrowHandler {

    private BorrowHandler next;

    // Returns next to allow fluent chaining: h1.setNext(h2).setNext(h3)
    public BorrowHandler setNext(BorrowHandler next) {
        this.next = next;
        return next;
    }

    public abstract void handle(BorrowContext context);

    protected void passToNext(BorrowContext context) {
        if (next != null) next.handle(context);
    }
}
