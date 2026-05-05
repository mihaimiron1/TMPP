package com.mihai.library.mediator;

import com.mihai.library.domain.Book;
import com.mihai.library.domain.LibraryItem;
import com.mihai.library.domain.Loan;
import com.mihai.library.domain.ReturnReceipt;
import com.mihai.library.factory.StandardLibraryFactory;
import com.mihai.library.memento.BorrowCartService;
import com.mihai.library.repo.Catalog;
import com.mihai.library.repo.LoanRepository;
import com.mihai.library.service.LibraryService;
import com.mihai.library.service.penalty.PenaltyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LibraryWorkflowMediatorTest {

    private TestCatalog catalog;
    private TestLoanRepository loans;
    private BorrowCartService borrowCartService;
    private LibraryService libraryService;
    private PenaltyService penaltyService;
    private LibraryWorkflowMediator mediator;

    @BeforeEach
    void setUp() {
        catalog = new TestCatalog();
        loans = new TestLoanRepository();
        borrowCartService = new BorrowCartService(catalog);
        libraryService = new LibraryService(catalog, loans, new StandardLibraryFactory().loanPolicy());
        penaltyService = new PenaltyService(catalog, loans, new StandardLibraryFactory().penaltyStrategy());
        mediator = new LibraryWorkflowMediator(borrowCartService, libraryService, penaltyService);
    }

    @Test
    void checkoutBorrowCart_borrowsItemsAndResetsCart() {
        catalog.addItem(book("B1", "Clean Code"));
        catalog.addItem(book("B2", "Effective Java"));
        borrowCartService.addItem("U1", "B1");
        borrowCartService.addItem("U1", "B2");

        List<Loan> checkedOut = mediator.checkoutBorrowCart("U1");

        assertEquals(2, checkedOut.size());
        assertTrue(checkedOut.stream().anyMatch(loan -> "B1".equals(loan.getItemId())));
        assertTrue(checkedOut.stream().anyMatch(loan -> "B2".equals(loan.getItemId())));
        assertEquals(List.of(), borrowCartService.getCartItems("U1"));
    }

    @Test
    void checkoutBorrowCart_whenCartIsEmpty_returnsEmptyList() {
        List<Loan> checkedOut = mediator.checkoutBorrowCart("U1");

        assertEquals(List.of(), checkedOut);
    }

    @Test
    void returnItemWithPenalty_combinesPenaltyAndReturnWorkflow() {
        catalog.addItem(book("B1", "Clean Code"));
        Loan loan = new Loan(
                "L1",
                "U1",
                "B1",
                LocalDate.now().minusDays(20),
                LocalDate.now().minusDays(2));
        loans.save(loan);

        ReturnReceipt receipt = mediator.returnItemWithPenalty("B1");

        assertEquals("L1", receipt.getLoan().getLoanId());
        assertNotNull(receipt.getLoan().getReturnDate());
        assertFalse(receipt.getLoan().isActive());
        assertEquals(BigDecimal.valueOf(3.00), receipt.getPenalty());
    }

    private static Book book(String id, String title) {
        return Book.builder()
                .id(id)
                .title(title)
                .author("Author")
                .isbn("978-0-00-000000-0")
                .build();
    }

    private static final class TestCatalog implements Catalog {
        private final Map<String, LibraryItem> items = new LinkedHashMap<>();

        @Override
        public void addItem(LibraryItem item) {
            items.put(item.getId(), item);
        }

        @Override
        public Optional<LibraryItem> findById(String id) {
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
            loans.put(loan.getLoanId(), loan);
        }

        @Override
        public Optional<Loan> findById(String loanId) {
            return Optional.ofNullable(loans.get(loanId));
        }

        @Override
        public Optional<Loan> findActiveLoanByItemId(String itemId) {
            return loans.values().stream()
                    .filter(loan -> itemId.equals(loan.getItemId()) && loan.isActive())
                    .findFirst();
        }

        @Override
        public List<Loan> findByMemberId(String memberId) {
            return loans.values().stream()
                    .filter(loan -> memberId.equals(loan.getMemberId()))
                    .toList();
        }
    }
}
