package com.mihai.library.observer;

import com.mihai.library.domain.Loan;

public interface LibraryObserver {
    default void onItemBorrowed(Loan loan) {
    }

    default void onItemReturned(Loan loan) {
    }
}
