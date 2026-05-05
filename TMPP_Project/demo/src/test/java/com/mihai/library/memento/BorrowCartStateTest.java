package com.mihai.library.memento;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BorrowCartStateTest {

    @Test
    void newCart_startsInEmptyState() {
        BorrowCart cart = new BorrowCart();

        assertEquals("EmptyBorrowCartState", cart.currentStateName());
        assertEquals(0, cart.getItemIds().size());
    }

    @Test
    void addItem_transitionsCartFromEmptyToActive() {
        BorrowCart cart = new BorrowCart();

        cart.addItem("B1");

        assertEquals("ActiveBorrowCartState", cart.currentStateName());
        assertEquals(1, cart.getItemIds().size());
    }

    @Test
    void removeLastItem_transitionsCartBackToEmpty() {
        BorrowCart cart = new BorrowCart();
        cart.addItem("B1");

        assertTrue(cart.removeItem("B1"));

        assertEquals("EmptyBorrowCartState", cart.currentStateName());
        assertTrue(cart.getItemIds().isEmpty());
    }

    @Test
    void clear_transitionsActiveCartToEmpty() {
        BorrowCart cart = new BorrowCart();
        cart.addItem("B1");
        cart.addItem("B2");

        cart.clear();

        assertEquals("EmptyBorrowCartState", cart.currentStateName());
        assertTrue(cart.getItemIds().isEmpty());
    }

    @Test
    void restore_nonEmptySnapshot_transitionsCartToActive() {
        BorrowCart cart = new BorrowCart();
        cart.addItem("B1");
        BorrowCartMemento snapshot = cart.save();
        cart.clear();

        cart.restore(snapshot);

        assertEquals("ActiveBorrowCartState", cart.currentStateName());
        assertEquals(1, cart.getItemIds().size());
    }

    @Test
    void restore_emptySnapshot_transitionsCartToEmpty() {
        BorrowCart cart = new BorrowCart();
        BorrowCartMemento emptySnapshot = cart.save();
        cart.addItem("B1");

        cart.restore(emptySnapshot);

        assertEquals("EmptyBorrowCartState", cart.currentStateName());
        assertTrue(cart.getItemIds().isEmpty());
    }

    @Test
    void emptyState_removeStillReturnsFalse() {
        BorrowCart cart = new BorrowCart();

        assertFalse(cart.removeItem("B1"));
        assertEquals("EmptyBorrowCartState", cart.currentStateName());
    }

    @Test
    void activeState_rejectsDuplicateItems() {
        BorrowCart cart = new BorrowCart();
        cart.addItem("B1");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> cart.addItem("B1"));

        assertEquals("Item already present in cart: B1", ex.getMessage());
        assertEquals("ActiveBorrowCartState", cart.currentStateName());
    }
}
