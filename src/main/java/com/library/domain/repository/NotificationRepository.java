package com.library.domain.repository;

import com.library.domain.model.Notification;

import java.util.List;

public interface NotificationRepository {
    Notification save(Notification notification);
    List<Notification> findByUserId(String userId);
    void markAsRead(String id);
}
