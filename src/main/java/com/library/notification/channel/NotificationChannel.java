package com.library.notification.channel;

import com.library.domain.enums.NotificationType;

// PATTERN: Bridge (implementor) — defines how a notification is physically delivered;
//          decoupled from what message is built (the abstraction side, AbstractLoanNotification)
public interface NotificationChannel {
    void deliver(String userId, String message, NotificationType type);
}
