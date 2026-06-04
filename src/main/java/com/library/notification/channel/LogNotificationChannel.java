package com.library.notification.channel;

import com.library.domain.enums.NotificationType;
import org.springframework.stereotype.Component;

// PATTERN: Bridge (concrete implementor) — prints notification to console; useful for debugging
@Component
public class LogNotificationChannel implements NotificationChannel {

    @Override
    public void deliver(String userId, String message, NotificationType type) {
        System.out.printf("[NOTIF][%s] userId=%s | %s%n", type, userId, message);
    }
}
