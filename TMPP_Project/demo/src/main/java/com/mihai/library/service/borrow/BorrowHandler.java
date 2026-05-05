package com.mihai.library.service.borrow;

public interface BorrowHandler {
    void handle(BorrowRequestContext context);

    BorrowHandler setNext(BorrowHandler next);
}
