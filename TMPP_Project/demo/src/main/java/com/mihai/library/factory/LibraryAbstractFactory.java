package com.mihai.library.factory;

import com.mihai.library.service.LoanPolicy;
import com.mihai.library.service.penalty.PenaltyStrategy;

public interface LibraryAbstractFactory {
    LibraryItemCreator bookCreator();

    LibraryItemCreator magazineCreator();

    LibraryItemCreator dvdCreator();

    LibraryItemCreator groupCreator();

    LoanPolicy loanPolicy();

    PenaltyStrategy penaltyStrategy();
}
