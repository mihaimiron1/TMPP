package com.mihai.library.notification;

public final class NoOpNotificationChannel implements NotificationChannel {
    @Override
    public void send(String memberId, String message) {
        // Intentionally left blank.
    }
}
