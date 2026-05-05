package com.mihai.library.visitor;

public final class CatalogStatistics {
    private final int books;
    private final int magazines;
    private final int dvds;
    private final int groups;
    private final int totalItems;

    CatalogStatistics(int books, int magazines, int dvds, int groups) {
        this.books = books;
        this.magazines = magazines;
        this.dvds = dvds;
        this.groups = groups;
        this.totalItems = books + magazines + dvds + groups;
    }

    public int getBooks() {
        return books;
    }

    public int getMagazines() {
        return magazines;
    }

    public int getDvds() {
        return dvds;
    }

    public int getGroups() {
        return groups;
    }

    public int getTotalItems() {
        return totalItems;
    }

    @Override
    public String toString() {
        return "CatalogStatistics{books=" + books +
                ", magazines=" + magazines +
                ", dvds=" + dvds +
                ", groups=" + groups +
                ", totalItems=" + totalItems + "}";
    }
}
