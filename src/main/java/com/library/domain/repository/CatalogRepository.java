package com.library.domain.repository;

import com.library.domain.enums.ItemType;
import com.library.domain.model.Book;
import com.library.domain.model.Dvd;
import com.library.domain.model.LibraryItem;
import com.library.domain.model.Magazine;

import java.util.List;
import java.util.Optional;

public interface CatalogRepository {
    List<LibraryItem> findAll();
    List<Book> findAllBooks();
    List<Magazine> findAllMagazines();
    List<Dvd> findAllDvds();
    Optional<LibraryItem> findById(String id, ItemType type);
    void updateAvailableQuantity(String id, ItemType type, int newAvailableQuantity);
}
