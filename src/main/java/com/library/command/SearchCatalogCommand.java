package com.library.command;

import com.library.domain.enums.ItemType;
import com.library.domain.model.LibraryItem;
import com.library.service.search.SearchService;

import java.util.List;

// PATTERN: Command (concrete) — encapsulates a catalog search with all its parameters
public class SearchCatalogCommand implements LibraryCommand<List<LibraryItem>> {

    private final SearchService searchService;
    private final String        userId;
    private final String        query;
    private final ItemType      type;
    private final String        genre;
    private final boolean       availableOnly;

    public SearchCatalogCommand(SearchService searchService, String userId,
                                String query, ItemType type, String genre, boolean availableOnly) {
        this.searchService = searchService;
        this.userId        = userId;
        this.query         = query;
        this.type          = type;
        this.genre         = genre;
        this.availableOnly = availableOnly;
    }

    @Override
    public List<LibraryItem> execute() {
        return searchService.search(userId, query, type, genre, availableOnly);
    }
}
