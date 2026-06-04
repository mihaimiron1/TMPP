package com.library.mediator;

import com.library.command.BorrowItemCommand;
import com.library.command.CommandInvoker;
import com.library.command.ReturnItemCommand;
import com.library.command.SearchCatalogCommand;
import com.library.domain.enums.ItemType;
import com.library.domain.model.LibraryItem;
import com.library.domain.model.Loan;
import com.library.domain.model.Notification;
import com.library.domain.model.User;
import com.library.service.auth.AuthService;
import com.library.service.loan.LoanService;
import com.library.service.notification.NotificationService;
import com.library.service.search.SearchService;
import com.library.web.dto.LoginResponse;
import org.springframework.stereotype.Component;

import java.util.List;

// PATTERN: Mediator (concrete) — orchestrates login (auth + status refresh + notification check),
//          borrow/return (via Command), and search (via Command + Memento)
@Component
public class DefaultLibraryMediator implements LibraryMediator {

    private final AuthService         authService;
    private final LoanService         loanService;
    private final NotificationService notificationService;
    private final SearchService       searchService;
    private final CommandInvoker      invoker;

    public DefaultLibraryMediator(AuthService authService,
                                  LoanService loanService,
                                  NotificationService notificationService,
                                  SearchService searchService,
                                  CommandInvoker invoker) {
        this.authService         = authService;
        this.loanService         = loanService;
        this.notificationService = notificationService;
        this.searchService       = searchService;
        this.invoker             = invoker;
    }

    @Override
    public LoginResponse login(String email, String password) {
        // 1. Authenticate via AuthService
        User user = authService.login(email, password);
        // 2. Sync loan statuses (fires overdue Observer events if needed)
        loanService.getUserLoans(user.getId());
        // 3. Generate DUE_SOON notifications for upcoming returns
        notificationService.checkDueSoonForUser(user.getId());
        long unread = notificationService.getUnreadCount(user.getId());
        return new LoginResponse(user.getId(), user.getName(), unread);
    }

    @Override
    public User register(String email, String password, String name) {
        return authService.register(email, password, name);
    }

    // PATTERN: Command — wraps borrow/return/search as Command objects via CommandInvoker
    @Override
    public Loan borrowItem(String userId, String itemId, ItemType itemType) {
        return invoker.invoke(new BorrowItemCommand(loanService, userId, itemId, itemType));
    }

    @Override
    public Loan returnItem(String loanId) {
        return invoker.invoke(new ReturnItemCommand(loanService, loanId));
    }

    @Override
    public List<LibraryItem> search(String userId, String query,
                                    ItemType type, String genre, boolean availableOnly) {
        return invoker.invoke(
            new SearchCatalogCommand(searchService, userId, query, type, genre, availableOnly));
    }

    @Override
    public List<Notification> getNotifications(String userId) {
        return notificationService.getUserNotifications(userId);
    }
}
