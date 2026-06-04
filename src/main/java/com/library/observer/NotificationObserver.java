package com.library.observer;

import com.library.domain.model.Loan;
import com.library.domain.repository.CatalogRepository;
import com.library.domain.repository.UserRepository;
import com.library.notification.BorrowConfirmationNotification;
import com.library.notification.OverdueNotification;
import com.library.notification.ReturnConfirmationNotification;
import com.library.notification.channel.UINotificationChannel;
import org.springframework.stereotype.Component;

// PATTERN: Observer (concrete) — reacts to loan events by persisting UI notifications
@Component
public class NotificationObserver implements LoanEventObserver {

    private final UserRepository    userRepo;
    private final CatalogRepository catalogRepo;
    private final UINotificationChannel uiChannel;

    public NotificationObserver(UserRepository userRepo,
                                CatalogRepository catalogRepo,
                                UINotificationChannel uiChannel) {
        this.userRepo    = userRepo;
        this.catalogRepo = catalogRepo;
        this.uiChannel   = uiChannel;
    }

    @Override
    public void onLoanBorrowed(Loan loan) {
        userRepo.findById(loan.getUserId()).ifPresent(user ->
            new BorrowConfirmationNotification(uiChannel).send(loan, user, resolveTitle(loan)));
    }

    @Override
    public void onLoanReturned(Loan loan) {
        userRepo.findById(loan.getUserId()).ifPresent(user ->
            new ReturnConfirmationNotification(uiChannel).send(loan, user, resolveTitle(loan)));
    }

    @Override
    public void onLoanOverdue(Loan loan) {
        userRepo.findById(loan.getUserId()).ifPresent(user ->
            new OverdueNotification(uiChannel).send(loan, user, resolveTitle(loan)));
    }

    private String resolveTitle(Loan loan) {
        return catalogRepo.findById(loan.getItemId(), loan.getItemType())
            .map(com.library.domain.model.LibraryItem::getTitle)
            .orElse("Unknown item");
    }
}
