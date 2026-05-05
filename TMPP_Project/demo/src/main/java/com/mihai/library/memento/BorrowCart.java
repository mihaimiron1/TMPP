package com.mihai.library.memento;

import java.util.ArrayList;
import java.util.List;

public final class BorrowCart {
    private final List<String> itemIds = new ArrayList<>();
    private BorrowCartState state = new EmptyBorrowCartState();

    public void addItem(String itemId) {
        if (itemId == null || itemId.isBlank()) {
            throw new IllegalArgumentException("itemId invalid");
        }
        state.addItem(this, itemId);
    }

    public boolean removeItem(String itemId) {
        if (itemId == null || itemId.isBlank()) {
            return false;
        }
        return state.removeItem(this, itemId);
    }

    public void clear() {
        state.clear(this);
    }

    public List<String> getItemIds() {
        return List.copyOf(itemIds);
    }

    public BorrowCartMemento save() {
        return new BorrowCartMemento(itemIds);
    }

    public void restore(BorrowCartMemento memento) {
        if (memento == null) {
            throw new IllegalArgumentException("memento null");
        }
        clearStoredItems();
        itemIds.addAll(memento.itemIds());
        syncStateWithContents();
    }

    void appendItem(String itemId) {
        itemIds.add(itemId);
    }

    boolean containsItem(String itemId) {
        return itemIds.contains(itemId);
    }

    boolean removeStoredItem(String itemId) {
        return itemIds.remove(itemId);
    }

    void clearStoredItems() {
        itemIds.clear();
    }

    boolean isEmpty() {
        return itemIds.isEmpty();
    }

    void transitionTo(BorrowCartState nextState) {
        if (nextState == null) {
            throw new IllegalArgumentException("nextState null");
        }
        this.state = nextState;
    }

    String currentStateName() {
        return state.getClass().getSimpleName();
    }

    private void syncStateWithContents() {
        if (itemIds.isEmpty()) {
            transitionTo(new EmptyBorrowCartState());
            return;
        }
        transitionTo(new ActiveBorrowCartState());
    }
}
