package com.library.service.search;

import com.library.domain.model.LibraryItem;
import com.library.domain.repository.CatalogRepository;
import com.library.domain.enums.ItemType;
import com.library.iterator.FilteredCatalogIterator;
import com.library.search.SearchHistory;
import com.library.search.SearchMemento;
import com.library.search.SearchState;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SearchService {

    private final CatalogRepository catalogRepo;
    // Per-user search history (in-memory; acceptable for university scope)
    private final Map<String, SearchHistory> histories = new ConcurrentHashMap<>();

    public SearchService(CatalogRepository catalogRepo) {
        this.catalogRepo = catalogRepo;
    }

    // PATTERN: Memento — saves current criteria before searching so user can undo
    // PATTERN: Iterator — FilteredCatalogIterator traverses the catalog applying the filter
    public List<LibraryItem> search(String userId, String query,
                                    ItemType type, String genre, boolean availableOnly) {
        SearchState state = new SearchState(query, type, genre, availableOnly);
        histories.computeIfAbsent(userId, k -> new SearchHistory()).push(state.save());
        return new FilteredCatalogIterator(catalogRepo.findAll(), state.toFilter()).toList();
    }

    // PATTERN: Memento — restores the previous search state from the caretaker stack
    public List<LibraryItem> undoSearch(String userId) {
        SearchHistory history = histories.get(userId);
        if (history == null || !history.canUndo()) return catalogRepo.findAll();
        SearchMemento memento = history.undo();
        SearchState state = new SearchState();
        state.restore(memento);
        return new FilteredCatalogIterator(catalogRepo.findAll(), state.toFilter()).toList();
    }

    public List<LibraryItem> getAllItems() {
        return catalogRepo.findAll();
    }
}
