package com.library.service.loan;

import com.library.domain.enums.ItemType;
import com.library.domain.enums.LoanStatus;
import com.library.domain.model.Loan;
import com.library.domain.repository.CatalogRepository;
import com.library.domain.repository.LoanRepository;
import com.library.domain.repository.UserRepository;
import com.library.factory.LoanPolicyFactory;
import com.library.factory.LoanPolicyFactoryProvider;
import com.library.service.borrow.*;
import com.library.service.loan.penalty.PenaltyStrategy;
import com.library.service.loan.policy.WeekendAdjustmentDecorator;
import com.library.observer.LoanEventPublisher;
import com.library.service.loan.state.LoanState;
import com.library.service.loan.state.LoanStateResolver;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final CatalogRepository  catalogRepo;
    private final LoanRepository     loanRepo;
    private final UserRepository     userRepo;
    private final LoanEventPublisher publisher;

    public LoanService(CatalogRepository catalogRepo,
                       LoanRepository loanRepo,
                       UserRepository userRepo,
                       LoanEventPublisher publisher) {
        this.catalogRepo = catalogRepo;
        this.loanRepo    = loanRepo;
        this.userRepo    = userRepo;
        this.publisher   = publisher;
    }

    // PATTERN: Chain of Responsibility — assembles the 5-handler validation chain
    private BorrowHandler buildBorrowChain() {
        BorrowHandler userExists   = new UserExistsHandler(userRepo);
        BorrowHandler itemExists   = new ItemExistsHandler(catalogRepo);
        BorrowHandler availability = new ItemAvailabilityHandler();
        BorrowHandler loansLimit   = new ActiveLoansLimitHandler(loanRepo);
        BorrowHandler overdueBlock = new OverdueLoanBlockHandler(loanRepo);

        userExists.setNext(itemExists)
                  .setNext(availability)
                  .setNext(loansLimit)
                  .setNext(overdueBlock);
        return userExists;
    }

    public Loan borrowItem(String userId, String itemId, ItemType itemType) {
        // Chain of Responsibility — all 5 preconditions must pass
        BorrowContext ctx = new BorrowContext(userId, itemId, itemType);
        buildBorrowChain().handle(ctx);

        // Abstract Factory — get matched duration policy + penalty strategy for this item type
        LoanPolicyFactory factory = LoanPolicyFactoryProvider.getFactory(itemType);

        // Decorator — wrap the base policy so weekends are never due dates
        var policy = new WeekendAdjustmentDecorator(factory.createDurationPolicy());

        // Builder — assemble the Loan object using the computed due date
        Loan loan = new LoanBuilder()
            .userId(userId)
            .itemId(itemId)
            .itemType(itemType)
            .applyPolicy(policy)
            .build();

        loanRepo.save(loan);
        catalogRepo.updateAvailableQuantity(
            itemId, itemType, ctx.getItem().getAvailableQuantity() - 1);

        // PATTERN: Observer — notify all subscribers (NotificationObserver, AuditLogObserver)
        publisher.publishBorrowed(loan);
        return loan;
    }

    public Loan returnItem(String loanId) {
        Loan loan = loanRepo.findById(loanId)
            .orElseThrow(() -> new RuntimeException("Loan not found: " + loanId));

        // State — check whether returning is permitted in the current state
        LoanState state = LoanStateResolver.resolve(loan, LocalDate.now());
        if (!state.canReturn()) {
            throw new RuntimeException("Cannot return in state: " + state.getStatusLabel());
        }

        // Strategy — calculate accumulated overdue penalty
        double penalty = calculatePenalty(loan);

        loan.setReturnDate(LocalDate.now());
        loan.setStatus(LoanStatus.RETURNED);
        loan.setPenaltyMDL(penalty);
        loanRepo.update(loan);

        // Restore one copy to available stock
        catalogRepo.findById(loan.getItemId(), loan.getItemType())
            .ifPresent(item -> catalogRepo.updateAvailableQuantity(
                item.getId(), item.getType(), item.getAvailableQuantity() + 1));

        // PATTERN: Observer — notify all subscribers
        publisher.publishReturned(loan);
        return loan;
    }

    // PATTERN: Strategy — delegates penalty calculation to the strategy matched by Abstract Factory
    public double calculatePenalty(Loan loan) {
        PenaltyStrategy strategy = LoanPolicyFactoryProvider
            .getFactory(loan.getItemType())
            .createPenaltyStrategy();
        return strategy.calculate(loan.getDueDate(), LocalDate.now());
    }

    public LoanState getLoanState(Loan loan) {
        return LoanStateResolver.resolve(loan, LocalDate.now());
    }

    public List<Loan> getUserLoans(String userId) {
        List<Loan> loans = loanRepo.findByUserId(userId);
        // Sync persisted status: active loans past due date become OVERDUE in Excel
        LocalDate today = LocalDate.now();
        for (Loan loan : loans) {
            if ((loan.getStatus() == LoanStatus.ACTIVE || loan.getStatus() == LoanStatus.EXTENDED)
                    && loan.getDueDate().isBefore(today)) {
                loan.setStatus(LoanStatus.OVERDUE);
                loanRepo.update(loan);
                publisher.publishOverdue(loan);
            }
        }
        return loans;
    }

    public List<Loan> getAllActiveLoans() {
        return loanRepo.findAllActive();
    }
}
