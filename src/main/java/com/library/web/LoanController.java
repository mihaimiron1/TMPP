package com.library.web;

import com.library.domain.enums.LoanStatus;
import com.library.domain.model.Loan;
import com.library.domain.repository.CatalogRepository;
import com.library.facade.LibraryFacade;
import com.library.factory.LoanPolicyFactoryProvider;
import com.library.web.dto.BorrowRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LibraryFacade      facade;
    private final CatalogRepository  catalogRepo;

    public LoanController(LibraryFacade facade, CatalogRepository catalogRepo) {
        this.facade      = facade;
        this.catalogRepo = catalogRepo;
    }

    @PostMapping
    public ResponseEntity<Loan> borrow(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody BorrowRequest req) {
        return ResponseEntity.ok(facade.borrowItem(userId, req.getItemId(), req.getItemType()));
    }

    @PutMapping("/{loanId}/return")
    public ResponseEntity<Loan> returnItem(@PathVariable String loanId) {
        return ResponseEntity.ok(facade.returnItem(loanId));
    }

    @GetMapping("/my")
    public List<Map<String, Object>> myLoans(@RequestHeader("X-User-Id") String userId) {
        return facade.getUserLoans(userId).stream()
            .map(loan -> enrichWithTitle(loan))
            .toList();
    }

    private Map<String, Object> enrichWithTitle(Loan loan) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id",           loan.getId());
        m.put("itemId",       loan.getItemId());
        m.put("itemType",     loan.getItemType());
        m.put("borrowDate",   loan.getBorrowDate());
        m.put("dueDate",      loan.getDueDate());
        m.put("returnDate",   loan.getReturnDate());
        m.put("status",       loan.getStatus());
        m.put("penaltyMDL",   loan.getPenaltyMDL());

        // Live penalty for overdue loans not yet returned
        m.put("currentPenaltyMDL", computeLivePenalty(loan));

        String title = catalogRepo.findById(loan.getItemId(), loan.getItemType())
            .map(i -> i.getTitle()).orElse(loan.getItemId());
        m.put("itemTitle", title);
        return m;
    }

    private double computeLivePenalty(Loan loan) {
        if (loan.getStatus() == LoanStatus.RETURNED) {
            return loan.getPenaltyMDL(); // final stored value
        }
        if (loan.getDueDate() != null && loan.getDueDate().isBefore(LocalDate.now())) {
            return LoanPolicyFactoryProvider.getFactory(loan.getItemType())
                .createPenaltyStrategy()
                .calculate(loan.getDueDate(), LocalDate.now());
        }
        return 0.0;
    }
}
