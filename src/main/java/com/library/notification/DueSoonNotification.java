package com.library.notification;

import com.library.domain.enums.NotificationType;
import com.library.domain.model.Loan;
import com.library.domain.model.User;
import com.library.notification.channel.NotificationChannel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// PATTERN: Template Method (concrete) — due-soon reminder sent when 3 days or fewer remain
public class DueSoonNotification extends AbstractLoanNotification {

    public DueSoonNotification(NotificationChannel channel) {
        super(channel);
    }

    @Override
    protected String buildSubject(Loan loan, String itemTitle) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), loan.getDueDate());
        return "Reminder: \"" + itemTitle + "\" due in " + days + " day(s)";
    }

    @Override
    protected String buildBody(Loan loan, User user, String itemTitle) {
        return "Please return it by " + loan.getDueDate() + " to avoid penalties.";
    }

    @Override
    protected NotificationType getNotificationType() {
        return NotificationType.DUE_SOON;
    }
}
