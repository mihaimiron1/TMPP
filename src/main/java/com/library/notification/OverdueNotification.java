package com.library.notification;

import com.library.domain.enums.NotificationType;
import com.library.domain.model.Loan;
import com.library.domain.model.User;
import com.library.notification.channel.NotificationChannel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// PATTERN: Template Method (concrete) — overdue alert with penalty info
public class OverdueNotification extends AbstractLoanNotification {

    public OverdueNotification(NotificationChannel channel) {
        super(channel);
    }

    @Override
    protected String buildSubject(Loan loan, String itemTitle) {
        long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now());
        return "OVERDUE: \"" + itemTitle + "\" (" + daysLate + " day(s) late)";
    }

    @Override
    protected String buildBody(Loan loan, User user, String itemTitle) {
        return "Please return it immediately. Penalty is accumulating daily.";
    }

    @Override
    protected NotificationType getNotificationType() {
        return NotificationType.OVERDUE;
    }
}
