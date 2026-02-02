package com.mihai.library.repo;

import java.util.List;
import java.util.Optional;

import com.mihai.library.domain.LibraryItem;

public interface Catalog {
    void addItem(LibraryItem item);
    Optional<LibraryItem> findById(String id);
    List<LibraryItem> getAllItems();
}