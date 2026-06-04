package com.library.iterator;

import com.library.domain.model.LibraryItem;

import java.util.ArrayList;
import java.util.List;

// PATTERN: Iterator (concrete) — advances through a catalog list, skipping items that don't match the filter
public class FilteredCatalogIterator implements CatalogIterator<LibraryItem> {

    private final List<LibraryItem> source;
    private final CatalogFilter filter;
    private int index = 0;
    private LibraryItem peeked;

    public FilteredCatalogIterator(List<LibraryItem> source, CatalogFilter filter) {
        this.source = source;
        this.filter = filter;
        advance();
    }

    private void advance() {
        peeked = null;
        while (index < source.size()) {
            LibraryItem candidate = source.get(index++);
            if (filter.matches(candidate)) {
                peeked = candidate;
                break;
            }
        }
    }

    @Override
    public boolean hasNext() { return peeked != null; }

    @Override
    public LibraryItem next() {
        LibraryItem result = peeked;
        advance();
        return result;
    }

    @Override
    public List<LibraryItem> toList() {
        List<LibraryItem> result = new ArrayList<>();
        while (hasNext()) result.add(next());
        return result;
    }
}
