package com.mihai.library.service;

import com.mihai.library.domain.Book;
import com.mihai.library.domain.Dvd;
import com.mihai.library.domain.LibraryItem;
import com.mihai.library.service.decorator.ItemTypeLoanPolicyDecorator;
import com.mihai.library.service.decorator.WeekendAdjustmentLoanPolicyDecorator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoanPolicyDecoratorTest {

    @Test
    void itemTypeDecorator_capsDvdToSevenDaysWhenBasePolicyIsLonger() {
        LoanPolicy basePolicy = new DefaultLoanPolicy(); // 14 zile
        LoanPolicy policy = new ItemTypeLoanPolicyDecorator(basePolicy);

        LibraryItem dvd = Dvd.builder()
                .id("D1")
                .title("Interstellar")
                .durationMinutes(169)
                .build();

        LocalDate dueDate = policy.computeDueDate(dvd, LocalDate.of(2026, 3, 1));
        assertEquals(LocalDate.of(2026, 3, 8), dueDate);
    }

    @Test
    void weekendDecorator_movesSaturdayDueDateToMonday() {
        LoanPolicy policy = new WeekendAdjustmentLoanPolicyDecorator(new DefaultLoanPolicy());

        LibraryItem book = Book.builder()
                .id("B1")
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .build();

        LocalDate dueDate = policy.computeDueDate(book, LocalDate.of(2026, 3, 7));
        assertEquals(LocalDate.of(2026, 3, 23), dueDate);
    }

    @Test
    void chainedDecorators_applyItemRuleThenWeekendAdjustment() {
        LoanPolicy policy = new WeekendAdjustmentLoanPolicyDecorator(
                new ItemTypeLoanPolicyDecorator(new DefaultLoanPolicy()));

        LibraryItem dvd = Dvd.builder()
                .id("D1")
                .title("Interstellar")
                .durationMinutes(169)
                .build();

        // 2026-03-01 + 7 zile = 2026-03-08 (duminica), apoi se ajusteaza la luni 2026-03-09
        LocalDate dueDate = policy.computeDueDate(dvd, LocalDate.of(2026, 3, 1));
        assertEquals(LocalDate.of(2026, 3, 9), dueDate);
    }
}
