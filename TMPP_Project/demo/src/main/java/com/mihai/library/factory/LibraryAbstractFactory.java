package com.mihai.library.factory;

import com.mihai.library.service.LoanPolicy;

public interface LibraryAbstractFactory {
    LibraryItemCreator bookCreator();

    LibraryItemCreator magazineCreator();

    LibraryItemCreator dvdCreator();

    LoanPolicy loanPolicy(); // obiect înrudit (familie)
}