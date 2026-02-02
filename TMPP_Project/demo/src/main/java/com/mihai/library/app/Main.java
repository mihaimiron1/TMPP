package com.mihai.library.app;

import com.mihai.library.domain.Book;
import com.mihai.library.domain.Dvd;
import com.mihai.library.domain.Magazine;
import com.mihai.library.repo.Catalog;
import com.mihai.library.repo.InMemoryCatalog;
import com.mihai.library.repo.InMemoryLoanRepository;
import com.mihai.library.repo.LoanRepository;
import com.mihai.library.service.DefaultLoanPolicy;
import com.mihai.library.service.LibraryService;
import com.mihai.library.service.LoanPolicy;

public final class Main {
    public static void main(String[] args) {
        Catalog catalog = new InMemoryCatalog();
        LoanRepository loans = new InMemoryLoanRepository();
        LoanPolicy policy = new DefaultLoanPolicy();
        LibraryService service = new LibraryService(catalog, loans, policy);

        // iteme
        catalog.addItem(new Book("B1", "Clean Code", "Robert C. Martin", "978-0132350884"));
        catalog.addItem(new Magazine("M1", "National Geographic", 202));
        catalog.addItem(new Dvd("D1", "Interstellar", 169));

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
