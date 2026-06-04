package com.library.notification;

import com.library.domain.enums.NotificationType;
import com.library.domain.model.Loan;
import com.library.domain.model.User;
import com.library.notification.channel.NotificationChannel;

// PATTERN: Template Method (concrete) — borrow confirmation message
public class BorrowConfirmationNotification extends AbstractLoanNotification {

    public BorrowConfirmationNotification(NotificationChannel channel) {
        super(channel);
    }

    @Override
    protected String buildSubject(Loan loan, String itemTitle) {
        return "Borrowed: \"" + itemTitle + "\"";
    }

    @Override
    protected String buildBody(Loan loan, User user, String itemTitle) {
        return "Return by " + loan.getDueDate() + ". Happy reading, " + user.getName() + "!";
    }

    @Override
    protected NotificationType getNotificationType() {
        return NotificationType.BORROW_CONFIRMED;
    }
}
