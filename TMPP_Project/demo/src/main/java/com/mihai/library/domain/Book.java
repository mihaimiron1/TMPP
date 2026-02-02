package com.mihai.library.domain;

public final class Book extends LibraryItem {
    private final String author;
    private final String isbn;

    public Book(String id, String title, String author, String isbn) {
        super(id, title);
        if (author == null || author.isBlank()) throw new IllegalArgumentException("author invalid");
        if (isbn == null || isbn.isBlank()) throw new IllegalArgumentException("isbn invalid");
        this.author = author;
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    @Override
    public String getType() {
        return "BOOK";
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") +
                ", author='" + author + "', isbn='" + isbn + "'}";
    }
}