package com.library.web.dto;

import com.library.domain.enums.ItemType;

public class BorrowRequest {
    private String   itemId;
    private ItemType itemType;

    public String   getItemId()   { return itemId; }
    public void     setItemId(String itemId) { this.itemId = itemId; }
    public ItemType getItemType() { return itemType; }
    public void     setItemType(ItemType itemType) { this.itemType = itemType; }
}
