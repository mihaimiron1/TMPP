package com.library.infrastructure.proxy;

import com.library.domain.enums.ItemType;
import com.library.domain.model.Book;
import com.library.domain.model.Dvd;
import com.library.domain.model.LibraryItem;
import com.library.domain.model.Magazine;
import com.library.domain.repository.CatalogRepository;
import com.library.infrastructure.excel.repository.CatalogExcelRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// PATTERN: Proxy — sits in front of CatalogExcelRepository and adds lazy-load + in-memory caching.
//          First access reads from Excel (slow); subsequent reads hit the cache (fast).
//          Cache is invalidated on any write so callers always see consistent data.
@Repository
@Primary
public class CatalogProxy implements CatalogRepository {

    private final CatalogExcelRepository real;

    private List<Book>        cachedBooks;
    private List<Magazine>    cachedMagazines;
    private List<Dvd>         cachedDvds;

    public CatalogProxy(CatalogExcelRepository real) {
        this.real = real;
    }

    @Override
    public synchronized List<Book> findAllBooks() {
        if (cachedBooks == null) {
            cachedBooks = real.findAllBooks();
        }
        return cachedBooks;
    }

    @Override
    public synchronized List<Magazine> findAllMagazines() {
        if (cachedMagazines == null) {
            cachedMagazines = real.findAllMagazines();
        }
        return cachedMagazines;
    }

    @Override
    public synchronized List<Dvd> findAllDvds() {
        if (cachedDvds == null) {
            cachedDvds = real.findAllDvds();
        }
        return cachedDvds;
    }

    @Override
    public List<LibraryItem> findAll() {
        List<LibraryItem> all = new ArrayList<>();
        all.addAll(findAllBooks());
        all.addAll(findAllMagazines());
        all.addAll(findAllDvds());
        return all;
    }

    // Uses the warm cache instead of hitting Excel for each lookup
    @Override
    public Optional<LibraryItem> findById(String id, ItemType type) {
        return findAll().stream()
            .filter(item -> id.equals(item.getId()) && item.getType() == type)
            .findFirst();
    }

    // Delegates write to the real repo, then invalidates cache so next read is fresh
    @Override
    public synchronized void updateAvailableQuantity(String id, ItemType type, int newAvailableQuantity) {
        real.updateAvailableQuantity(id, type, newAvailableQuantity);
        invalidateCache();
    }

    private void invalidateCache() {
        cachedBooks     = null;
        cachedMagazines = null;
        cachedDvds      = null;
    }
}
