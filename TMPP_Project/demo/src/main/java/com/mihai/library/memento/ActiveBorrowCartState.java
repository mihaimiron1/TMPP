package com.mihai.library.memento;

final class ActiveBorrowCartState implements BorrowCartState {
    @Override
    public void addItem(BorrowCart cart, String itemId) {
        if (cart.containsItem(itemId)) {
            throw new IllegalArgumentException("Item already present in cart: " + itemId);
        }
        cart.appendItem(itemId);
    }

    @Override
    public boolean removeItem(BorrowCart cart, String itemId) {
        boolean removed = cart.removeStoredItem(itemId);
        if (cart.isEmpty()) {
            cart.transitionTo(new EmptyBorrowCartState());
        }
        return removed;
    }

    @Override
    public void clear(BorrowCart cart) {
        cart.clearStoredItems();
        cart.transitionTo(new EmptyBorrowCartState());
    }
}
