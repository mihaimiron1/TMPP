package com.mihai.library.facade;

import com.mihai.library.domain.Book;
import com.mihai.library.domain.LibraryItem;
import com.mihai.library.domain.LibraryItemGroup;
import com.mihai.library.domain.Loan;
import com.mihai.library.factory.StandardLibraryFactory;
import com.mihai.library.repo.Catalog;
import com.mihai.library.repo.LoanRepository;
import com.mihai.library.service.exceptions.ItemAlreadyLoanedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LibraryFacadeTest {

    private LibraryFacade facade;

    @BeforeEach
    void setUp() {
        Catalog catalog = new TestCatalog();
        LoanRepository loanRepository = new TestLoanRepository();
        facade = new LibraryFacade(catalog, loanRepository, new StandardLibraryFactory());
    }

    @Test
    void addBook_addsBookToCatalog() {
        facade.addBook("B1", "Clean Code", "Robert C. Martin", "978-0132350884");

        LibraryItem item = facade.findItemById("B1").orElseThrow();
        Book book = assertInstanceOf(Book.class, item);

        assertEquals("Clean Code", book.getTitle());
        assertEquals("Robert C. Martin", book.getAuthor());
        assertEquals("978-0132350884", book.getIsbn());
    }

    @Test
    void borrowItem_createsLoanAndSavesIt() {
        facade.addBook("B1", "Clean Code", "Robert C. Martin", "978-0132350884");

        Loan loan = facade.borrowItem("U1", "B1");

        assertEquals("U1", loan.getMemberId());
        assertEquals("B1", loan.getItemId());
        assertNotNull(loan.getLoanId());
        assertEquals(1, facade.listLoansForMember("U1").size());
    }

    @Test
    void borrowItem_whenAlreadyLoaned_throws() {
        facade.addBook("B1", "Clean Code", "Robert C. Martin", "978-0132350884");
        facade.borrowItem("U1", "B1");

        assertThrows(ItemAlreadyLoanedException.class, () -> facade.borrowItem("U2", "B1"));
    }

    @Test
    void returnItem_marksLoanAsReturned() {
        facade.addBook("B1", "Clean Code", "Robert C. Martin", "978-0132350884");
        facade.borrowItem("U1", "B1");

        Loan returnedLoan = facade.returnItem("B1");

        assertFalse(returnedLoan.isActive());
        assertNotNull(returnedLoan.getReturnDate());
    }

    @Test
    void addGroupByItemIds_createsCompositeGroup() {
        facade.addBook("B1", "Clean Code", "Robert C. Martin", "978-0132350884");
        facade.addMagazine("M1", "National Geographic", 202);

        facade.addGroupByItemIds("G1", "Starter Bundle", List.of("B1", "M1"));

        LibraryItem item = facade.findItemById("G1").orElseThrow();
        LibraryItemGroup group = assertInstanceOf(LibraryItemGroup.class, item);

        assertEquals(2, group.getChildren().size());
    }

    @Test
    void addGroupByItemIds_whenChildMissing_throws() {
        facade.addBook("B1", "Clean Code", "Robert C. Martin", "978-0132350884");

        assertThrows(IllegalArgumentException.class,
                () -> facade.addGroupByItemIds("G1", "Starter Bundle", List.of("B1", "M1")));
    }

    @Test
    void ensureDemoCatalog_isIdempotent() {
        facade.ensureDemoCatalog();
        facade.ensureDemoCatalog();

        List<LibraryItem> items = facade.listCatalogItems();
        assertEquals(4, items.size());
        assertTrue(items.stream().anyMatch(item -> "B1".equals(item.getId())));
        assertTrue(items.stream().anyMatch(item -> "M1".equals(item.getId())));
        assertTrue(items.stream().anyMatch(item -> "D1".equals(item.getId())));
        assertTrue(items.stream().anyMatch(item -> "G1".equals(item.getId())));
    }

    @Test
    void closeActiveLoanIfPresent_returnsOperationStatus() {
        facade.addBook("B1", "Clean Code", "Robert C. Martin", "978-0132350884");

        assertFalse(facade.closeActiveLoanIfPresent("B1"));

        facade.borrowItem("U1", "B1");

        assertTrue(facade.closeActiveLoanIfPresent("B1"));
        assertFalse(facade.closeActiveLoanIfPresent("B1"));
    }

    private static final class TestCatalog implements Catalog {
        private final Map<String, LibraryItem> items = new LinkedHashMap<>();

        @Override
        public void addItem(LibraryItem item) {
            if (item == null) {
                throw new IllegalArgumentException("item null");
            }
            items.put(item.getId(), item);
        }

        @Override
        public Optional<LibraryItem> findById(String id) {
            if (id == null || id.isBlank()) {
                return Optional.empty();
            }
            return Optional.ofNullable(items.get(id));
        }

        @Override
        public List<LibraryItem> getAllItems() {
            return items.values().stream().toList();
        }
    }

    private static final class TestLoanRepository implements LoanRepository {
        private final Map<String, Loan> loans = new LinkedHashMap<>();

        @Override
        public void save(Loan loan) {
            if (loan == null) {
                throw new IllegalArgumentException("loan null");
            }
            loans.put(loan.getLoanId(), loan);
        }

        @Override
        public Optional<Loan> findById(String loanId) {
            if (loanId == null || loanId.isBlank()) {
                return Optional.empty();
            }
            return Optional.ofNullable(loans.get(loanId));
        }

        @Override
        public Optional<Loan> findActiveLoanByItemId(String itemId) {
            if (itemId == null || itemId.isBlank()) {
                return Optional.empty();
            }

            return loans.values().stream()
                    .filter(loan -> itemId.equals(loan.getItemId()) && loan.isActive())
                    .findFirst();
        }

        @Override
        public List<Loan> findByMemberId(String memberId) {
            if (memberId == null || memberId.isBlank()) {
                return List.of();
            }

            return loans.values().stream()
                    .filter(loan -> memberId.equals(loan.getMemberId()))
                    .toList();
        }
    }
}
