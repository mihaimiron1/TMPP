package com.mihai.library.observer;

import com.mihai.library.domain.Loan;
import com.mihai.library.notification.NotificationChannel;
import com.mihai.library.service.penalty.PenaltyService;

import java.math.BigDecimal;

public final class PenaltyObserver implements LibraryObserver {
    private final PenaltyService penaltyService;
    private final NotificationChannel channel;

    public PenaltyObserver(PenaltyService penaltyService, NotificationChannel channel) {
        if (penaltyService == null) {
            throw new IllegalArgumentException("penaltyService null");
        }
        if (channel == null) {
            throw new IllegalArgumentException("channel null");
        }
        this.penaltyService = penaltyService;
        this.channel = channel;
    }

    @Override
    public void onItemReturned(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("loan null");
        }
        if (loan.getReturnDate() == null) {
            throw new IllegalArgumentException("returnDate missing");
        }

        BigDecimal penalty = penaltyService.calculatePenaltyForLoan(loan.getLoanId(), loan.getReturnDate());
        channel.send(loan.getMemberId(), "Penalty for item " + loan.getItemId() + " is " + penalty);
    }
}
