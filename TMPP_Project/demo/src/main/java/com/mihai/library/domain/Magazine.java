package com.mihai.library.domain;

public final class Magazine extends LibraryItem {
    private final int issueNumber;

    public Magazine(String id, String title, int issueNumber) {
        super(id, title);
        if (issueNumber <= 0) throw new IllegalArgumentException("issueNumber invalid");
        this.issueNumber = issueNumber;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    @Override
    public String getType() {
        return "MAGAZINE";
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") +
                ", issueNumber=" + issueNumber + "}";
    }
}