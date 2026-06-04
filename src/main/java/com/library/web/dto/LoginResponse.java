package com.library.web.dto;

public class LoginResponse {
    private final String userId;
    private final String name;
    private final long   unreadNotifications;

    public LoginResponse(String userId, String name, long unreadNotifications) {
        this.userId               = userId;
        this.name                 = name;
        this.unreadNotifications  = unreadNotifications;
    }

    public String getUserId()              { return userId; }
    public String getName()                { return name; }
    public long   getUnreadNotifications() { return unreadNotifications; }
}
