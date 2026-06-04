package com.library.notification;

import com.library.domain.enums.NotificationType;
import com.library.domain.model.Loan;
import com.library.domain.model.User;
import com.library.notification.channel.NotificationChannel;

// PATTERN: Template Method — fixed skeleton (send); abstract hooks define the specific message content
// PATTERN: Bridge (abstraction) — holds a NotificationChannel; delegates delivery to the implementor
public abstract class AbstractLoanNotification {

    private final NotificationChannel channel;

    protected AbstractLoanNotification(NotificationChannel channel) {
        this.channel = channel;
    }

    // Template method — invariant flow: build subject → build body → deliver via Bridge
    public final void send(Loan loan, User user, String itemTitle) {
        String message = buildSubject(loan, itemTitle) + " — " + buildBody(loan, user, itemTitle);
        channel.deliver(user.getId(), message, getNotificationType());
    }

    protected abstract String buildSubject(Loan loan, String itemTitle);
    protected abstract String buildBody(Loan loan, User user, String itemTitle);
    protected abstract NotificationType getNotificationType();
}
