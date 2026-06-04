package com.library.service.borrow;

import com.library.domain.enums.ItemType;
import com.library.domain.model.LibraryItem;
import com.library.domain.model.User;

// Carries the borrow request data through the chain; enriched by each handler that loads domain objects
public class BorrowContext {

    private final String userId;
    private final String itemId;
    private final ItemType itemType;
    private User user;
    private LibraryItem item;

    public BorrowContext(String userId, String itemId, ItemType itemType) {
        this.userId   = userId;
        this.itemId   = itemId;
        this.itemType = itemType;
    }

    public String getUserId()             { return userId; }
    public String getItemId()             { return itemId; }
    public ItemType getItemType()         { return itemType; }

    public User getUser()                 { return user; }
    public void setUser(User user)        { this.user = user; }

    public LibraryItem getItem()          { return item; }
    public void setItem(LibraryItem item) { this.item = item; }
}
