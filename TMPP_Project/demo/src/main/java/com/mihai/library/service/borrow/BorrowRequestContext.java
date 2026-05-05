package com.mihai.library.service.borrow;

import com.mihai.library.domain.LibraryItem;

public final class BorrowRequestContext {
    private final String memberId;
    private final String itemId;
    private LibraryItem item;

    public BorrowRequestContext(String memberId, String itemId) {
        this.memberId = memberId;
        this.itemId = itemId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getItemId() {
        return itemId;
    }

    public LibraryItem getItem() {
        return item;
    }

    public void setItem(LibraryItem item) {
        if (item == null) {
            throw new IllegalArgumentException("item null");
        }
        this.item = item;
    }
}
