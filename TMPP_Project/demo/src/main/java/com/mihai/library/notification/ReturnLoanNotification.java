package com.mihai.library.notification;

import com.mihai.library.domain.Loan;

public final class ReturnLoanNotification extends LoanNotification {
    public ReturnLoanNotification(NotificationChannel channel) {
        super(channel);
    }

    @Override
    protected String buildMessage(Loan loan) {
        return "Returned item " + loan.getItemId() + " on " + loan.getReturnDate();
    }
}
