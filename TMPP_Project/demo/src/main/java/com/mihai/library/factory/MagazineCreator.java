package com.mihai.library.factory;

import com.mihai.library.domain.LibraryItem;
import com.mihai.library.domain.Magazine;

public final class MagazineCreator extends LibraryItemCreator {
    @Override
    protected LibraryItem createItem(ItemRequest r) {
        if (r.getIssueNumber() == null || r.getIssueNumber() <= 0)
            throw new IllegalArgumentException("issueNumber invalid");
        return new Magazine(r.getId(), r.getTitle(), r.getIssueNumber());
    }
}
