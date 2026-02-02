package com.mihai.library.domain;

public abstract class LibraryItem {
    private final String id;
    private final String title;

    protected LibraryItem(String id, String title) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id invalid");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("title invalid");
        this.id = id;
        this.title = title;
    }

    public final String getId() {
        return id;
    }

    public final String getTitle() {
        return title;
    }

    public abstract String getType();

    @Override
    public String toString() {
        return getType() + "{id='" + id + "', title='" + title + "'}";
    }
}