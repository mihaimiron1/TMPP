package com.library.search;

import com.library.domain.enums.ItemType;

// PATTERN: Memento — immutable snapshot of a SearchState; only SearchState and SearchHistory can use it
public final class SearchMemento {

    private final String   query;
    private final ItemType typeFilter;
    private final String   genreFilter;
    private final boolean  availableOnly;

    // Package-private: only SearchState creates mementos
    SearchMemento(String query, ItemType typeFilter, String genreFilter, boolean availableOnly) {
        this.query         = query;
        this.typeFilter    = typeFilter;
        this.genreFilter   = genreFilter;
        this.availableOnly = availableOnly;
    }

    String   getQuery()        { return query; }
    ItemType getTypeFilter()   { return typeFilter; }
    String   getGenreFilter()  { return genreFilter; }
    boolean  isAvailableOnly() { return availableOnly; }
}
