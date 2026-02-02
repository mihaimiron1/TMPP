package com.mihai.library.domain;

public final class Dvd extends LibraryItem {
    private final int durationMinutes;

    public Dvd(String id, String title, int durationMinutes) {
        super(id, title);
        if (durationMinutes <= 0) throw new IllegalArgumentException("durationMinutes invalid");
        this.durationMinutes = durationMinutes;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    @Override
    public String getType() {
        return "DVD";
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") +
                ", durationMinutes=" + durationMinutes + "}";
    }
}