package com.mihai.library.memento;

interface BorrowCartState {
    void addItem(BorrowCart cart, String itemId);

    boolean removeItem(BorrowCart cart, String itemId);

    void clear(BorrowCart cart);
}
