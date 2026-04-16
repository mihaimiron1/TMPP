package com.mihai.library.iterator;

import com.mihai.library.domain.LibraryItem;

@FunctionalInterface
public interface LibraryItemCriteria {
    boolean matches(LibraryItem item);
}
