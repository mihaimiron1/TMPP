package com.library.mediator;

import com.library.domain.enums.ItemType;
import com.library.domain.model.LibraryItem;
import com.library.domain.model.Loan;
import com.library.domain.model.Notification;
import com.library.domain.model.User;
import com.library.web.dto.LoginResponse;

import java.util.List;

// PATTERN: Mediator — coordinates workflows that span multiple services;
//          services communicate through the mediator instead of calling each other directly
public interface LibraryMediator {
    LoginResponse login(String email, String password);
    User register(String email, String password, String name);
    Loan borrowItem(String userId, String itemId, ItemType itemType);
    Loan returnItem(String loanId);
    List<LibraryItem> search(String userId, String query, ItemType type, String genre, boolean availableOnly);
    List<Notification> getNotifications(String userId);
}
