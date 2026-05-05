package com.mihai.library.mediator;

import com.mihai.library.domain.Loan;
import com.mihai.library.domain.ReturnReceipt;

import java.util.List;

public interface CirculationMediator {
    ReturnReceipt returnItemWithPenalty(String itemId);

    List<Loan> checkoutBorrowCart(String memberId);
}
