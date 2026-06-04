package com.library.notification.channel;

import com.library.domain.enums.NotificationType;
import com.library.domain.model.Notification;
import com.library.domain.repository.NotificationRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

// PATTERN: Bridge (concrete implementor) — persists the notification to the Excel Notifications sheet
@Component
public class UINotificationChannel implements NotificationChannel {

    private final NotificationRepository notifRepo;

    public UINotificationChannel(NotificationRepository notifRepo) {
        this.notifRepo = notifRepo;
    }

    @Override
    public void deliver(String userId, String message, NotificationType type) {
        Notification n = new Notification(
            UUID.randomUUID().toString(), userId, message, type, LocalDate.now());
        notifRepo.save(n);
    }
}
