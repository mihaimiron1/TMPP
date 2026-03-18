package com.mihai.library.app;

import com.mihai.library.adapter.FileCatalogAdapter;
import com.mihai.library.adapter.FileLoanRepositoryAdapter;
import com.mihai.library.adapter.storage.FileStorage;
import com.mihai.library.factory.*;
import com.mihai.library.repo.Catalog;
import com.mihai.library.repo.LoanRepository;
import com.mihai.library.service.LibraryService;
import com.mihai.library.service.LoanPolicy;

import com.mihai.library.service.exceptions.LoanNotFoundException;
import com.mihai.library.domain.LibraryItem;
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
                if (catalog.findById("B1").isEmpty()) {
                        catalog.addItem(factory.bookCreator().create(
                                ItemRequest.builder(ItemType.BOOK, "B1", "Clean Code")
                                        .author("Robert C. Martin")
                                        .isbn("978-0132350884")
                                        .build()));
                }

                if (catalog.findById("M1").isEmpty()) {
                        catalog.addItem(factory.magazineCreator().create(
                                ItemRequest.builder(ItemType.MAGAZINE, "M1", "National Geographic")
                                        .issueNumber(202)
                                        .build()));
                }

                if (catalog.findById("D1").isEmpty()) {
                        catalog.addItem(factory.dvdCreator().create(
                                ItemRequest.builder(ItemType.DVD, "D1", "Interstellar")
                                        .durationMinutes(169)
                                        .build()));
                }
        }
}
