package com.library.domain.model;

import com.library.domain.enums.ItemType;

public class Dvd extends LibraryItem {

    public static final int DEFAULT_LOAN_DAYS = 5;
    public static final double DEFAULT_PENALTY_MDL = 2.0;

    private String director;
    private int durationMinutes;

    public Dvd() {}

    public Dvd(String id, String title, String director, String genre,
               int durationMinutes, int year, int quantity,
               int availableQuantity, double dailyPenaltyMDL) {
        super(id, title, genre, year, quantity, availableQuantity, dailyPenaltyMDL);
        this.director = director;
        this.durationMinutes = durationMinutes;
    }

    @Override
    public ItemType getType() { return ItemType.DVD; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    // PATTERN: Prototype
    @Override
    public LibraryItem clone() {
        Dvd copy = new Dvd();
        copyTo(copy);
        copy.setDirector(this.director);
        copy.setDurationMinutes(this.durationMinutes);
        return copy;
    }

    @Override
    public String getDescription() {
        return "DVD: " + getTitle() + " directed by " + director + " (" + getYear() + ")";
    }
}
