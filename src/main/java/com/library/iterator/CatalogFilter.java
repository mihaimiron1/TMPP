package com.library.iterator;

import com.library.domain.model.LibraryItem;

// PATTERN: Iterator (filter predicate) — functional interface used by FilteredCatalogIterator
@FunctionalInterface
public interface CatalogFilter {
    boolean matches(LibraryItem item);
}
