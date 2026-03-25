package com.mihai.library.notification;

public interface NotificationChannel {
    void send(String memberId, String message);
}
