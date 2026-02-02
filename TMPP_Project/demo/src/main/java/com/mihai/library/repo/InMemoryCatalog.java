package com.mihai.library.repo;

import java.util.*;

import com.mihai.library.domain.LibraryItem;

public final class InMemoryCatalog implements Catalog {
    private final Map<String, LibraryItem> items = new HashMap<>();

    @Override
    public void addItem(LibraryItem item) {
        if (item == null) throw new IllegalArgumentException("item null");
        items.put(item.getId(), item);
    }

    @Override
    public Optional<LibraryItem> findById(String id) {
        if (id == null || id.isBlank()) return Optional.empty();
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<LibraryItem> getAllItems() {
        return new ArrayList<>(items.values());
    }
}