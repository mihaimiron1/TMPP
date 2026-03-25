package com.mihai.library.service;

import com.mihai.library.domain.Book;
import com.mihai.library.domain.LibraryItem;
import com.mihai.library.domain.Loan;
import com.mihai.library.notification.BorrowLoanNotification;
import com.mihai.library.notification.LoanNotification;
import com.mihai.library.notification.NotificationChannel;
import com.mihai.library.notification.ReturnLoanNotification;
import com.mihai.library.repo.Catalog;
import com.mihai.library.repo.LoanRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LibraryServiceNotificationBridgeTest {

    @Test
    void borrowItem_sendsBorrowNotificationThroughChannel() {
        TestCatalog catalog = new TestCatalog();
        TestLoanRepository loans = new TestLoanRepository();
        CapturingChannel channel = new CapturingChannel();

        catalog.addItem(Book.builder()
                .id("B1")
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .build());

        LibraryService service = new LibraryService(
                catalog,
                loans,
                new DefaultLoanPolicy(),
                new BorrowLoanNotification(channel),
                new ReturnLoanNotification(channel));

        service.borrowItem("U1", "B1");

        assertEquals(1, channel.messages.size());
        assertTrue(channel.messages.get(0).contains("[U1] Borrowed item B1"));
    }

    @Test
    void returnItem_sendsReturnNotificationThroughChannel() {
        TestCatalog catalog = new TestCatalog();
        TestLoanRepository loans = new TestLoanRepository();
        CapturingChannel channel = new CapturingChannel();

        catalog.addItem(Book.builder()
                .id("B1")
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .build());

        LibraryService service = new LibraryService(
                catalog,
                loans,
                new DefaultLoanPolicy(),
                new BorrowLoanNotification(channel),
                new ReturnLoanNotification(channel));

        service.borrowItem("U1", "B1");
        service.returnItem("B1");

        assertEquals(2, channel.messages.size());
        assertTrue(channel.messages.get(1).contains("[U1] Returned item B1"));
    }

    private static final class CapturingChannel implements NotificationChannel {
        private final List<String> messages = new ArrayList<>();

        @Override
        public void send(String memberId, String message) {
            messages.add("[" + memberId + "] " + message);
        }
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
