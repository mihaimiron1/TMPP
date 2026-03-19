package com.mihai.library.app;

import com.mihai.library.domain.LibraryItem;
import com.mihai.library.domain.LibraryItemGroup;
import com.mihai.library.facade.LibraryFacade;
import com.mihai.library.factory.LibraryAbstractFactory;
import com.mihai.library.factory.StandardLibraryFactory;

import java.nio.file.Path;

public final class Main {
    private static final Path DATA_DIRECTORY = Path.of("data");

    public static void main(String[] args) {
        LibraryAbstractFactory factory = new StandardLibraryFactory();
        // You can switch to: new ShortLoanLibraryFactory();

        LibraryFacade facade = LibraryFacade.fileBacked(DATA_DIRECTORY, factory);

        facade.ensureDemoCatalog();
        printCompositeDemo(facade);
        closeActiveDemoLoans(facade);

        String memberId = "U1";

        System.out.println("=== Borrow B1 ===");
        System.out.println(facade.borrowItem(memberId, "B1"));

        System.out.println("\n=== Borrow M1 ===");
        System.out.println(facade.borrowItem(memberId, "M1"));

        System.out.println("\n=== Return B1 ===");
        System.out.println(facade.returnItem("B1"));

        System.out.println("\n=== Loans for member U1 ===");
        facade.listLoansForMember(memberId).forEach(System.out::println);
    }

    private static void closeActiveDemoLoans(LibraryFacade facade) {
        facade.closeActiveLoanIfPresent("B1");
        facade.closeActiveLoanIfPresent("M1");
    }

    private static void printCompositeDemo(LibraryFacade facade) {
        System.out.println("=== Composite G1 in catalog ===");
        LibraryItem groupItem = facade.findItemById("G1")
                .orElseThrow(() -> new IllegalStateException("Missing seed item: G1"));

        if (groupItem instanceof LibraryItemGroup group) {
            System.out.println(group);
            System.out.println("Leaf item ids in G1: " + group.flattenLeafItems().stream().map(LibraryItem::getId).toList());
            return;
        }

        throw new IllegalStateException("Expected G1 to be a composite group");
    }
}
