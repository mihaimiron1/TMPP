package com.library.domain.model;

import com.library.domain.enums.ItemType;
import com.library.domain.prototype.Prototype;
import com.library.flyweight.FlyweightFactory;
import com.library.flyweight.GenreFlyweight;

import java.util.UUID;

// PATTERN: Prototype — abstract clone() enables copying catalog items without knowing their concrete type
// PATTERN: Composite — implementing CatalogComponent makes leaves and LibraryItemGroup interchangeable
public abstract class LibraryItem implements Prototype<LibraryItem>, CatalogComponent {

    private String id;
    private String title;
    private String genre;
    private int year;
    private int quantity;
    private int availableQuantity;
    private double dailyPenaltyMDL;

    protected LibraryItem() {}

    protected LibraryItem(String id, String title, String genre, int year,
                          int quantity, int availableQuantity, double dailyPenaltyMDL) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
        this.dailyPenaltyMDL = dailyPenaltyMDL;
    }

    public abstract ItemType getType();

    // PATTERN: Prototype — each subclass copies its specific fields; new UUID ensures identity uniqueness
    @Override
    public abstract LibraryItem clone();

    // PATTERN: Composite — leaf nodes (Book, Magazine, Dvd) return true; LibraryItemGroup returns false
    @Override
    public boolean isLeaf() { return true; }

    @Override
    public abstract String getDescription();

    protected void copyTo(LibraryItem target) {
        target.setId(UUID.randomUUID().toString());
        target.setTitle(this.title);
        target.setGenre(this.genre);
        target.setYear(this.year);
        target.setQuantity(1);
        target.setAvailableQuantity(1);
        target.setDailyPenaltyMDL(this.dailyPenaltyMDL);
    }

    public boolean isAvailable() {
        return availableQuantity > 0;
    }

    // PATTERN: Flyweight — genre object is shared across all items of the same genre
    public GenreFlyweight getGenreFlyweight() {
        return FlyweightFactory.getGenre(this.genre);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }

    public double getDailyPenaltyMDL() { return dailyPenaltyMDL; }
    public void setDailyPenaltyMDL(double dailyPenaltyMDL) { this.dailyPenaltyMDL = dailyPenaltyMDL; }
}
