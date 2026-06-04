package com.library.domain.model;

import com.library.domain.enums.ItemType;
import com.library.domain.enums.LoanStatus;

import java.time.LocalDate;

public class Loan {

    private String id;
    private String userId;
    private String itemId;
    private ItemType itemType;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus status;
    private double penaltyMDL;

    public Loan() {}

    public Loan(String id, String userId, String itemId, ItemType itemType,
                LocalDate borrowDate, LocalDate dueDate, LoanStatus status) {
        this.id = id;
        this.userId = userId;
        this.itemId = itemId;
        this.itemType = itemType;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = status;
        this.penaltyMDL = 0.0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public ItemType getItemType() { return itemType; }
    public void setItemType(ItemType itemType) { this.itemType = itemType; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public LoanStatus getStatus() { return status; }
    public void setStatus(LoanStatus status) { this.status = status; }

    public double getPenaltyMDL() { return penaltyMDL; }
    public void setPenaltyMDL(double penaltyMDL) { this.penaltyMDL = penaltyMDL; }
}
