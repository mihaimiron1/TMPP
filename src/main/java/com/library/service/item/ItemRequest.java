package com.library.service.item;

import com.library.domain.enums.ItemType;

// Transfer object carrying all possible fields for item creation; unused fields are simply ignored
public class ItemRequest {

    private ItemType type;
    private String title;
    private String genre;
    private int year;
    private int quantity;
    private double dailyPenaltyMDL;

    // Book
    private String author;
    private String isbn;

    // Magazine
    private String publisher;
    private int issueNumber;
    private String month;

    // DVD
    private String director;
    private int durationMinutes;

    public ItemType getType() { return type; }
    public void setType(ItemType type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getDailyPenaltyMDL() { return dailyPenaltyMDL; }
    public void setDailyPenaltyMDL(double dailyPenaltyMDL) { this.dailyPenaltyMDL = dailyPenaltyMDL; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public int getIssueNumber() { return issueNumber; }
    public void setIssueNumber(int issueNumber) { this.issueNumber = issueNumber; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
}
