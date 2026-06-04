package com.library.domain.model;

import com.library.domain.enums.NotificationType;

import java.time.LocalDate;

public class Notification {

    private String id;
    private String userId;
    private String message;
    private NotificationType type;
    private LocalDate createdAt;
    private boolean read;

    public Notification() {}

    public Notification(String id, String userId, String message,
                        NotificationType type, LocalDate createdAt) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.createdAt = createdAt;
        this.read = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}
