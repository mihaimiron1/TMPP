package com.mihai.library.facade;

import com.mihai.library.adapter.FileCatalogAdapter;
import com.mihai.library.adapter.FileLoanRepositoryAdapter;
import com.mihai.library.adapter.storage.FileStorage;
import com.mihai.library.domain.LibraryItem;
import com.mihai.library.domain.Loan;
import com.mihai.library.factory.ItemRequest;
import com.mihai.library.factory.ItemType;
import com.mihai.library.factory.LibraryAbstractFactory;
import com.mihai.library.repo.Catalog;
import com.mihai.library.repo.LoanRepository;
import com.mihai.library.service.LibraryService;
import com.mihai.library.service.exceptions.LoanNotFoundException;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public final class LibraryFacade {
    private final Catalog catalog;
    private final LibraryAbstractFactory factory;
    private final LibraryService libraryService;

    public LibraryFacade(Catalog catalog, LoanRepository loanRepository, LibraryAbstractFactory factory) {
        if (catalog == null) {
            throw new IllegalArgumentException("catalog null");
        }
        if (loanRepository == null) {
            throw new IllegalArgumentException("loanRepository null");
        }
        if (factory == null) {
            throw new IllegalArgumentException("factory null");
        }

        this.catalog = catalog;
        this.factory = factory;
        this.libraryService = new LibraryService(catalog, loanRepository, factory.loanPolicy());
    }

    public static LibraryFacade fileBacked(Path dataDirectory, LibraryAbstractFactory factory) {
        if (dataDirectory == null) {
            throw new IllegalArgumentException("dataDirectory null");
        }

        Path catalogFile = dataDirectory.resolve("catalog.db");
        Path loansFile = dataDirectory.resolve("loans.db");

        Catalog catalog = new FileCatalogAdapter(new FileStorage(catalogFile));
        LoanRepository loanRepository = new FileLoanRepositoryAdapter(new FileStorage(loansFile));
        return new LibraryFacade(catalog, loanRepository, factory);
    }

    public Loan borrowItem(String memberId, String itemId) {
        return libraryService.borrowItem(memberId, itemId);
    }

    public Loan returnItem(String itemId) {
        return libraryService.returnItem(itemId);
    }

    public List<Loan> listLoansForMember(String memberId) {
        return libraryService.listLoansForMember(memberId);
    }

    public boolean closeActiveLoanIfPresent(String itemId) {
        try {
            libraryService.returnItem(itemId);
            return true;
        } catch (LoanNotFoundException ex) {
            return false;
        }
    }

    public List<LibraryItem> listCatalogItems() {
        return catalog.getAllItems();
    }

    public Optional<LibraryItem> findItemById(String itemId) {
        if (itemId == null || itemId.isBlank()) {
            return Optional.empty();
        }
        return catalog.findById(itemId);
    }

    public void addBook(String id, String title, String author, String isbn) {
        catalog.addItem(factory.bookCreator().create(
                ItemRequest.builder(ItemType.BOOK, id, title)
                        .author(author)
                        .isbn(isbn)
                        .build()));
    }

    public void addMagazine(String id, String title, int issueNumber) {
        catalog.addItem(factory.magazineCreator().create(
                ItemRequest.builder(ItemType.MAGAZINE, id, title)
                        .issueNumber(issueNumber)
                        .build()));
    }

    public void addDvd(String id, String title, int durationMinutes) {
        catalog.addItem(factory.dvdCreator().create(
                ItemRequest.builder(ItemType.DVD, id, title)
                        .durationMinutes(durationMinutes)
                        .build()));
    }

    public void addGroupByItemIds(String groupId, String title, List<String> childItemIds) {
        if (childItemIds == null || childItemIds.isEmpty()) {
            throw new IllegalArgumentException("childItemIds invalid");
        }

        ItemRequest.Builder requestBuilder = ItemRequest.builder(ItemType.GROUP, groupId, title);
        for (String childItemId : childItemIds) {
            requestBuilder.child(requireCatalogItem(childItemId));
        }

        catalog.addItem(factory.groupCreator().create(requestBuilder.build()));
    }

    public void ensureDemoCatalog() {
        ensureBook();
        ensureMagazine();
        ensureDvd();
        ensureStarterGroup();
    }

    private void ensureBook() {
        if (catalog.findById("B1").isEmpty()) {
            addBook("B1", "Clean Code", "Robert C. Martin", "978-0132350884");
        }
    }

    private void ensureMagazine() {
        if (catalog.findById("M1").isEmpty()) {
            addMagazine("M1", "National Geographic", 202);
        }
    }

    private void ensureDvd() {
        if (catalog.findById("D1").isEmpty()) {
            addDvd("D1", "Interstellar", 169);
        }
    }

    private void ensureStarterGroup() {
        if (catalog.findById("G1").isPresent()) {
            return;
        }
        addGroupByItemIds("G1", "Starter Bundle", List.of("B1", "M1"));
    }

    private LibraryItem requireCatalogItem(String itemId) {
        if (itemId == null || itemId.isBlank()) {
            throw new IllegalArgumentException("itemId invalid");
        }
        return catalog.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Missing catalog item: " + itemId));
    }
}
