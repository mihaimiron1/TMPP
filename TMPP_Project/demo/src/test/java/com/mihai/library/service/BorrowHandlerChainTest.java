package com.mihai.library.service;

import com.mihai.library.domain.Book;
import com.mihai.library.domain.LibraryItem;
import com.mihai.library.domain.LibraryItemGroup;
import com.mihai.library.domain.Loan;
import com.mihai.library.repo.Catalog;
import com.mihai.library.repo.LoanRepository;
import com.mihai.library.service.borrow.BaseBorrowHandler;
import com.mihai.library.service.borrow.BorrowHandler;
import com.mihai.library.service.borrow.BorrowRequestContext;
import com.mihai.library.service.borrow.BorrowableItemHandler;
import com.mihai.library.service.borrow.ItemAvailabilityHandler;
import com.mihai.library.service.borrow.ItemIdValidationHandler;
import com.mihai.library.service.borrow.ItemLookupHandler;
import com.mihai.library.service.borrow.MemberIdValidationHandler;
import com.mihai.library.service.exceptions.ItemAlreadyLoanedException;
import com.mihai.library.service.exceptions.ItemNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BorrowHandlerChainTest {

    @Test
    void chain_stopsWhenMemberIdIsInvalid() {
        MemberIdValidationHandler first = new MemberIdValidationHandler();
        RecordingHandler next = new RecordingHandler();
        first.setNext(next);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> first.handle(new BorrowRequestContext(" ", "B1")));

        assertEquals("memberId invalid", ex.getMessage());
        assertFalse(next.called);
    }

    @Test
    void itemLookupHandler_loadsItemIntoContext() {
        TestCatalog catalog = new TestCatalog();
        Book book = Book.builder()
                .id("B1")
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .build();
        catalog.addItem(book);

        BorrowRequestContext context = new BorrowRequestContext("U1", "B1");
        new ItemLookupHandler(catalog).handle(context);

        assertSame(book, context.getItem());
    }

    @Test
    void itemLookupHandler_throwsWhenItemIsMissing() {
        ItemLookupHandler handler = new ItemLookupHandler(new TestCatalog());

        assertThrows(ItemNotFoundException.class, () -> handler.handle(new BorrowRequestContext("U1", "B1")));
    }

    @Test
    void borrowableItemHandler_rejectsCompositeItems() {
        BorrowRequestContext context = new BorrowRequestContext("U1", "G1");
        context.setItem(LibraryItemGroup.builder()
                .id("G1")
                .title("Starter Bundle")
                .child(Book.builder()
                        .id("B1")
                        .title("Clean Code")
                        .author("Robert C. Martin")
                        .isbn("978-0132350884")
                        .build())
                .build());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new BorrowableItemHandler().handle(context));

        assertTrue(ex.getMessage().contains("Composite items cannot be borrowed directly"));
    }

    @Test
    void itemAvailabilityHandler_rejectsAlreadyLoanedItems() {
        TestLoanRepository loans = new TestLoanRepository();
        loans.save(new Loan("L1", "U1", "B1", LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 15)));

        BorrowRequestContext context = new BorrowRequestContext("U2", "B1");

        assertThrows(ItemAlreadyLoanedException.class,
                () -> new ItemAvailabilityHandler(loans).handle(context));
    }

    @Test
    void fullChain_passesContextToEndForBorrowableAvailableItem() {
        TestCatalog catalog = new TestCatalog();
        TestLoanRepository loans = new TestLoanRepository();
        RecordingHandler tail = new RecordingHandler();
        Book book = Book.builder()
                .id("B1")
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .build();
        catalog.addItem(book);

        BorrowHandler chain = new MemberIdValidationHandler();
        chain.setNext(new ItemIdValidationHandler())
                .setNext(new ItemLookupHandler(catalog))
                .setNext(new BorrowableItemHandler())
                .setNext(new ItemAvailabilityHandler(loans))
                .setNext(tail);

        BorrowRequestContext context = new BorrowRequestContext("U1", "B1");
        chain.handle(context);

        assertTrue(tail.called);
        assertSame(book, context.getItem());
    }

    private static final class RecordingHandler extends BaseBorrowHandler {
        private boolean called;

        @Override
        public void handle(BorrowRequestContext context) {
            called = true;
            next(context);
        }
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
