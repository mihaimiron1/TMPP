package com.mihai.library.app;

import com.mihai.library.adapter.FileCatalogAdapter;
import com.mihai.library.adapter.FileLoanRepositoryAdapter;
import com.mihai.library.adapter.storage.FileStorage;
import com.mihai.library.domain.LibraryItem;
import com.mihai.library.domain.LibraryItemGroup;
import com.mihai.library.factory.ItemRequest;
import com.mihai.library.factory.ItemType;
import com.mihai.library.factory.LibraryAbstractFactory;
import com.mihai.library.factory.StandardLibraryFactory;
import com.mihai.library.repo.Catalog;
import com.mihai.library.repo.LoanRepository;
import com.mihai.library.service.LibraryService;
import com.mihai.library.service.LoanPolicy;
import com.mihai.library.service.exceptions.LoanNotFoundException;

import java.nio.file.Path;

public final class Main {
    private static final Path DATA_DIRECTORY = Path.of("data");
    private static final Path CATALOG_FILE = DATA_DIRECTORY.resolve("catalog.db");
    private static final Path LOANS_FILE = DATA_DIRECTORY.resolve("loans.db");

    public static void main(String[] args) {
        LibraryAbstractFactory factory = new StandardLibraryFactory();
        // You can switch to: new ShortLoanLibraryFactory();

        Catalog catalog = createCatalog();
        LoanRepository loans = createLoanRepository();
        LoanPolicy policy = factory.loanPolicy();

        LibraryService service = new LibraryService(catalog, loans, policy);

        seedCatalog(catalog, factory);
        printCompositeDemo(catalog);
        closeActiveDemoLoans(service);

        String memberId = "U1";

        System.out.println("=== Borrow B1 ===");
        System.out.println(service.borrowItem(memberId, "B1"));

        System.out.println("\n=== Borrow M1 ===");
        System.out.println(service.borrowItem(memberId, "M1"));

        System.out.println("\n=== Return B1 ===");
        System.out.println(service.returnItem("B1"));

        System.out.println("\n=== Loans for member U1 ===");
        service.listLoansForMember(memberId).forEach(System.out::println);
    }

    private static void closeActiveDemoLoans(LibraryService service) {
        closeLoanIfPresent(service, "B1");
        closeLoanIfPresent(service, "M1");
    }

    private static void closeLoanIfPresent(LibraryService service, String itemId) {
        try {
            service.returnItem(itemId);
        } catch (LoanNotFoundException ignored) {
            // No active loan exists for this item.
        }
    }

    private static Catalog createCatalog() {
        return new FileCatalogAdapter(new FileStorage(CATALOG_FILE));
    }

    private static LoanRepository createLoanRepository() {
        return new FileLoanRepositoryAdapter(new FileStorage(LOANS_FILE));
    }

    private static void seedCatalog(Catalog catalog, LibraryAbstractFactory factory) {
        seedBook(catalog, factory);
        seedMagazine(catalog, factory);
        seedDvd(catalog, factory);
        seedStarterGroup(catalog, factory);
    }

    private static void seedBook(Catalog catalog, LibraryAbstractFactory factory) {
        if (catalog.findById("B1").isEmpty()) {
            catalog.addItem(factory.bookCreator().create(
                    ItemRequest.builder(ItemType.BOOK, "B1", "Clean Code")
                            .author("Robert C. Martin")
                            .isbn("978-0132350884")
                            .build()));
        }
    }

    private static void seedMagazine(Catalog catalog, LibraryAbstractFactory factory) {
        if (catalog.findById("M1").isEmpty()) {
            catalog.addItem(factory.magazineCreator().create(
                    ItemRequest.builder(ItemType.MAGAZINE, "M1", "National Geographic")
                            .issueNumber(202)
                            .build()));
        }
    }

    private static void seedDvd(Catalog catalog, LibraryAbstractFactory factory) {
        if (catalog.findById("D1").isEmpty()) {
            catalog.addItem(factory.dvdCreator().create(
                    ItemRequest.builder(ItemType.DVD, "D1", "Interstellar")
                            .durationMinutes(169)
                            .build()));
        }
    }

    private static void seedStarterGroup(Catalog catalog, LibraryAbstractFactory factory) {
        if (catalog.findById("G1").isPresent()) {
            return;
        }

        LibraryItem book = requireItem(catalog, "B1");
        LibraryItem magazine = requireItem(catalog, "M1");

        catalog.addItem(factory.groupCreator().create(
                ItemRequest.builder(ItemType.GROUP, "G1", "Starter Bundle")
                        .child(book)
                        .child(magazine)
                        .build()));
    }

    private static LibraryItem requireItem(Catalog catalog, String itemId) {
        return catalog.findById(itemId)
                .orElseThrow(() -> new IllegalStateException("Missing seed item: " + itemId));
    }

    private static void printCompositeDemo(Catalog catalog) {
        System.out.println("=== Composite G1 in catalog ===");
        LibraryItem groupItem = requireItem(catalog, "G1");
        if (groupItem instanceof LibraryItemGroup group) {
            System.out.println(group);
            System.out.println("Leaf item ids in G1: " + group.flattenLeafItems().stream().map(LibraryItem::getId).toList());
            return;
        }

        throw new IllegalStateException("Expected G1 to be a composite group");
    }
}
