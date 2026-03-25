package com.mihai.library.notification;

import com.mihai.library.domain.Loan;

public final class BorrowLoanNotification extends LoanNotification {
    public BorrowLoanNotification(NotificationChannel channel) {
        super(channel);
    }

    @Override
    protected String buildMessage(Loan loan) {
        return "Borrowed item " + loan.getItemId() + " (due " + loan.getDueDate() + ")";
    }
}
