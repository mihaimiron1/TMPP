package com.mihai.library.app;

import com.mihai.library.factory.*;
import com.mihai.library.repo.Catalog;
import com.mihai.library.repo.InMemoryCatalog;
import com.mihai.library.repo.InMemoryLoanRepository;
import com.mihai.library.repo.LoanRepository;
import com.mihai.library.service.LibraryService;
import com.mihai.library.service.LoanPolicy;

public final class Main {
    public static void main(String[] args) {

        // ABSTRACT FACTORY
        LibraryAbstractFactory factory = new StandardLibraryFactory();
        // Poți schimba cu: new ShortLoanLibraryFactory();

        Catalog catalog = new InMemoryCatalog();
        LoanRepository loans = new InMemoryLoanRepository();
        LoanPolicy policy = factory.loanPolicy();

        LibraryService service = new LibraryService(catalog, loans, policy);

        // 🔽 FACTORY METHOD folosit prin creatori
        catalog.addItem(factory.bookCreator().create(
                ItemRequest.builder(ItemType.BOOK, "B1", "Clean Code")
                        .author("Robert C. Martin")
                        .isbn("978-0132350884")
                        .build()));

        catalog.addItem(factory.magazineCreator().create(
                ItemRequest.builder(ItemType.MAGAZINE, "M1", "National Geographic")
                        .issueNumber(202)
                        .build()));

        catalog.addItem(factory.dvdCreator().create(
                ItemRequest.builder(ItemType.DVD, "D1", "Interstellar")
                        .durationMinutes(169)
                        .build()));

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
}
