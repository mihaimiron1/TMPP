package com.library.domain.model;

import com.library.domain.enums.ItemType;
import com.library.flyweight.AuthorFlyweight;
import com.library.flyweight.FlyweightFactory;

public class Book extends LibraryItem {

    public static final int DEFAULT_LOAN_DAYS = 14;
    public static final double DEFAULT_PENALTY_MDL = 1.5;

    private String author;
    private String isbn;

    public Book() {}

    public Book(String id, String title, String author, String isbn, String genre,
                int year, int quantity, int availableQuantity, double dailyPenaltyMDL) {
        super(id, title, genre, year, quantity, availableQuantity, dailyPenaltyMDL);
        this.author = author;
        this.isbn = isbn;
    }

    @Override
    public ItemType getType() { return ItemType.BOOK; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    // PATTERN: Prototype — creates a deep copy with a fresh ID (used when adding a new physical copy to stock)
    @Override
    public LibraryItem clone() {
        Book copy = new Book();
        copyTo(copy);
        copy.setAuthor(this.author);
        copy.setIsbn(this.isbn);
        return copy;
    }

    @Override
    public String getDescription() {
        return "Book: " + getTitle() + " by " + author + " (" + getYear() + ")";
    }

    // PATTERN: Flyweight — author instance is shared across all books by the same author
    public AuthorFlyweight getAuthorFlyweight() {
        return FlyweightFactory.getAuthor(this.author);
    }
}
