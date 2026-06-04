package com.library.facade;

import com.library.domain.enums.ItemType;
import com.library.domain.model.LibraryItem;
import com.library.domain.model.Loan;
import com.library.domain.model.Notification;
import com.library.domain.model.User;
import com.library.mediator.LibraryMediator;
import com.library.service.loan.LoanService;
import com.library.service.notification.NotificationService;
import com.library.service.search.SearchService;
import com.library.web.dto.LoginResponse;
import org.springframework.stereotype.Component;

import java.util.List;

// PATTERN: Facade — single entry point for all REST controllers; hides the internal service/mediator complexity
@Component
public class LibraryFacade {

    private final LibraryMediator      mediator;
    private final LoanService          loanService;
    private final NotificationService  notificationService;
    private final SearchService        searchService;

    public LibraryFacade(LibraryMediator mediator,
                         LoanService loanService,
                         NotificationService notificationService,
                         SearchService searchService) {
        this.mediator            = mediator;
        this.loanService         = loanService;
        this.notificationService = notificationService;
        this.searchService       = searchService;
    }

    // ── Auth ─────────────────────────────────────────────────────────────────

    public LoginResponse login(String email, String password) {
        return mediator.login(email, password);
    }

    public User register(String email, String password, String name) {
        return mediator.register(email, password, name);
    }

    // ── Catalog ───────────────────────────────────────────────────────────────

    public List<LibraryItem> getAllItems() {
        return searchService.getAllItems();
    }

    public List<LibraryItem> search(String userId, String query,
                                    ItemType type, String genre, boolean availableOnly) {
        return mediator.search(userId, query, type, genre, availableOnly);
    }

    public List<LibraryItem> undoSearch(String userId) {
        return searchService.undoSearch(userId);
    }

    // ── Loans ─────────────────────────────────────────────────────────────────

    public Loan borrowItem(String userId, String itemId, ItemType itemType) {
        return mediator.borrowItem(userId, itemId, itemType);
    }

    public Loan returnItem(String loanId) {
        return mediator.returnItem(loanId);
    }

    public List<Loan> getUserLoans(String userId) {
        return loanService.getUserLoans(userId);
    }

    // ── Notifications ─────────────────────────────────────────────────────────

    public List<Notification> getUserNotifications(String userId) {
        return notificationService.getUserNotifications(userId);
    }

    public void markNotificationAsRead(String notifId) {
        notificationService.markAsRead(notifId);
    }
}
