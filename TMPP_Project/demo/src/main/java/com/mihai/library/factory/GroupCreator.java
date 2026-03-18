package com.mihai.library.factory;

import com.mihai.library.domain.LibraryItem;
import com.mihai.library.domain.LibraryItemGroup;

import java.util.List;

public final class GroupCreator extends LibraryItemCreator {
    @Override
    protected LibraryItem createItem(ItemRequest r) {
        List<LibraryItem> children = r.getChildren();
        if (children.isEmpty()) {
            throw new IllegalArgumentException("group must contain at least one child");
        }

        LibraryItemGroup.Builder builder = LibraryItemGroup.builder()
                .id(r.getId())
                .title(r.getTitle());

        for (LibraryItem child : children) {
            builder.child(child);
        }

        return builder.build();
    }
}
