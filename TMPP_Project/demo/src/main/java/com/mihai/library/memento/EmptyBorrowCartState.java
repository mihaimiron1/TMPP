package com.mihai.library.memento;

final class EmptyBorrowCartState implements BorrowCartState {
    @Override
    public void addItem(BorrowCart cart, String itemId) {
        cart.appendItem(itemId);
        cart.transitionTo(new ActiveBorrowCartState());
    }

    @Override
    public boolean removeItem(BorrowCart cart, String itemId) {
        return false;
    }

    @Override
    public void clear(BorrowCart cart) {
        // Already empty.
    }
}
