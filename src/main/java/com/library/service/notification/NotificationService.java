package com.library.service.notification;

import com.library.domain.enums.LoanStatus;
import com.library.domain.model.Notification;
import com.library.domain.repository.CatalogRepository;
import com.library.domain.repository.LoanRepository;
import com.library.domain.repository.NotificationRepository;
import com.library.domain.repository.UserRepository;
import com.library.notification.DueSoonNotification;
import com.library.notification.channel.UINotificationChannel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notifRepo;
    private final LoanRepository         loanRepo;
    private final UserRepository         userRepo;
    private final CatalogRepository      catalogRepo;
    private final UINotificationChannel  uiChannel;

    public NotificationService(NotificationRepository notifRepo,
                               LoanRepository loanRepo,
                               UserRepository userRepo,
                               CatalogRepository catalogRepo,
                               UINotificationChannel uiChannel) {
        this.notifRepo   = notifRepo;
        this.loanRepo    = loanRepo;
        this.userRepo    = userRepo;
        this.catalogRepo = catalogRepo;
        this.uiChannel   = uiChannel;
    }

    // Called at login — generates DUE_SOON notifications for loans due within 3 days
    public void checkDueSoonForUser(String userId) {
        LocalDate today         = LocalDate.now();
        LocalDate soonThreshold = today.plusDays(3);

        loanRepo.findByUserId(userId).stream()
            .filter(l -> l.getStatus() == LoanStatus.ACTIVE
                      || l.getStatus() == LoanStatus.EXTENDED)
            .filter(l -> !l.getDueDate().isAfter(soonThreshold) && !l.getDueDate().isBefore(today))
            .forEach(loan -> userRepo.findById(loan.getUserId()).ifPresent(user -> {
                String title = catalogRepo.findById(loan.getItemId(), loan.getItemType())
                    .map(i -> i.getTitle()).orElse("item");
                new DueSoonNotification(uiChannel).send(loan, user, title);
            }));
    }

    public List<Notification> getUserNotifications(String userId) {
        return notifRepo.findByUserId(userId);
    }

    public long getUnreadCount(String userId) {
        return notifRepo.findByUserId(userId).stream().filter(n -> !n.isRead()).count();
    }

    public void markAsRead(String notifId) {
        notifRepo.markAsRead(notifId);
    }
}
