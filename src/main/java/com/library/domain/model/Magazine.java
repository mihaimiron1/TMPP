package com.library.domain.model;

import com.library.domain.enums.ItemType;

public class Magazine extends LibraryItem {

    public static final int DEFAULT_LOAN_DAYS = 7;
    public static final double DEFAULT_PENALTY_MDL = 0.5;

    private String publisher;
    private int issueNumber;
    private String month;

    public Magazine() {}

    public Magazine(String id, String title, String publisher, int issueNumber,
                    String month, String genre, int year, int quantity,
                    int availableQuantity, double dailyPenaltyMDL) {
        super(id, title, genre, year, quantity, availableQuantity, dailyPenaltyMDL);
        this.publisher = publisher;
        this.issueNumber = issueNumber;
        this.month = month;
    }

    @Override
    public ItemType getType() { return ItemType.MAGAZINE; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public int getIssueNumber() { return issueNumber; }
    public void setIssueNumber(int issueNumber) { this.issueNumber = issueNumber; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    // PATTERN: Prototype
    @Override
    public LibraryItem clone() {
        Magazine copy = new Magazine();
        copyTo(copy);
        copy.setPublisher(this.publisher);
        copy.setIssueNumber(this.issueNumber);
        copy.setMonth(this.month);
        return copy;
    }

    @Override
    public String getDescription() {
        return "Magazine: " + getTitle() + " Issue #" + issueNumber + " (" + month + " " + getYear() + ")";
    }
}
