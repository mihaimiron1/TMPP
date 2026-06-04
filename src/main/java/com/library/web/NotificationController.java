package com.library.web;

import com.library.domain.model.Notification;
import com.library.facade.LibraryFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final LibraryFacade facade;

    public NotificationController(LibraryFacade facade) {
        this.facade = facade;
    }

    @GetMapping
    public List<Notification> getNotifications(@RequestHeader("X-User-Id") String userId) {
        return facade.getUserNotifications(userId);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable String id) {
        facade.markNotificationAsRead(id);
        return ResponseEntity.ok().build();
    }
}
