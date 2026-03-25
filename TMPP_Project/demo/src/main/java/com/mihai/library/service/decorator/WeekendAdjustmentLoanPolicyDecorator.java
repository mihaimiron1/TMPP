package com.mihai.library.service.decorator;

import com.mihai.library.domain.LibraryItem;
import com.mihai.library.service.LoanPolicy;

import java.time.DayOfWeek;
import java.time.LocalDate;

public final class WeekendAdjustmentLoanPolicyDecorator extends LoanPolicyDecorator {
    public WeekendAdjustmentLoanPolicyDecorator(LoanPolicy delegate) {
        super(delegate);
    }

    @Override
    public LocalDate computeDueDate(LibraryItem item, LocalDate loanDate) {
        LocalDate dueDate = delegate.computeDueDate(item, loanDate);
        while (isWeekend(dueDate)) {
            dueDate = dueDate.plusDays(1);
        }
        return dueDate;
    }

    private static boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
}
