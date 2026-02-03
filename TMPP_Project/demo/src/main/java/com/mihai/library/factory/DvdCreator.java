package com.mihai.library.factory;

import com.mihai.library.domain.Dvd;
import com.mihai.library.domain.LibraryItem;

public final class DvdCreator extends LibraryItemCreator {
    @Override
    protected LibraryItem createItem(ItemRequest r) {
        if (r.getDurationMinutes() == null || r.getDurationMinutes() <= 0)
            throw new IllegalArgumentException("durationMinutes invalid");
        return new Dvd(r.getId(), r.getTitle(), r.getDurationMinutes());
    }
}
