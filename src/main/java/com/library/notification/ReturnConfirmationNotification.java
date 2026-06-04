package com.library.notification;

import com.library.domain.enums.NotificationType;
import com.library.domain.model.Loan;
import com.library.domain.model.User;
import com.library.notification.channel.NotificationChannel;

// PATTERN: Template Method (concrete) — return receipt with optional penalty summary
public class ReturnConfirmationNotification extends AbstractLoanNotification {

    public ReturnConfirmationNotification(NotificationChannel channel) {
        super(channel);
    }

    @Override
    protected String buildSubject(Loan loan, String itemTitle) {
        return "Returned: \"" + itemTitle + "\"";
    }

    @Override
    protected String buildBody(Loan loan, User user, String itemTitle) {
        if (loan.getPenaltyMDL() > 0) {
            return String.format("Thank you! Late penalty: %.2f MDL.", loan.getPenaltyMDL());
        }
        return "Thank you for returning on time!";
    }

    @Override
    protected NotificationType getNotificationType() {
        return NotificationType.RETURN_CONFIRMED;
    }
}
