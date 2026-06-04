package com.library.search;

import com.library.domain.enums.ItemType;
import com.library.domain.model.Book;
import com.library.domain.model.LibraryItem;
import com.library.iterator.CatalogFilter;

// PATTERN: Memento (originator) — holds current search parameters and knows how to save/restore them
public class SearchState {

    private String   query         = "";
    private ItemType typeFilter    = null;
    private String   genreFilter   = null;
    private boolean  availableOnly = false;

    public SearchState() {}

    public SearchState(String query, ItemType typeFilter, String genreFilter, boolean availableOnly) {
        this.query         = query;
        this.typeFilter    = typeFilter;
        this.genreFilter   = genreFilter;
        this.availableOnly = availableOnly;
    }

    // PATTERN: Memento — captures current state into an immutable snapshot
    public SearchMemento save() {
        return new SearchMemento(query, typeFilter, genreFilter, availableOnly);
    }

    // PATTERN: Memento — restores state from a previously saved snapshot
    public void restore(SearchMemento memento) {
        this.query         = memento.getQuery();
        this.typeFilter    = memento.getTypeFilter();
        this.genreFilter   = memento.getGenreFilter();
        this.availableOnly = memento.isAvailableOnly();
    }

    // Converts current state into a CatalogFilter for the Iterator
    public CatalogFilter toFilter() {
        return item -> matchesQuery(item) && matchesType(item)
                    && matchesGenre(item) && matchesAvailability(item);
    }

    private boolean matchesQuery(LibraryItem item) {
        if (query == null || query.isBlank()) return true;
        String q = query.toLowerCase();
        if (item.getTitle().toLowerCase().contains(q)) return true;
        if (item instanceof Book book) return book.getAuthor().toLowerCase().contains(q);
        return false;
    }

    private boolean matchesType(LibraryItem item) {
        return typeFilter == null || item.getType() == typeFilter;
    }

    private boolean matchesGenre(LibraryItem item) {
        return genreFilter == null || genreFilter.isBlank()
            || item.getGenre().equalsIgnoreCase(genreFilter);
    }

    private boolean matchesAvailability(LibraryItem item) {
        return !availableOnly || item.isAvailable();
    }

    public String   getQuery()        { return query; }
    public ItemType getTypeFilter()   { return typeFilter; }
    public String   getGenreFilter()  { return genreFilter; }
    public boolean  isAvailableOnly() { return availableOnly; }
}
