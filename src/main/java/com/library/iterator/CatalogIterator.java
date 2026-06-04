package com.library.iterator;

import java.util.List;

// PATTERN: Iterator — custom iterator contract for traversing catalog collections
public interface CatalogIterator<T> {
    boolean hasNext();
    T next();
    List<T> toList();
}
