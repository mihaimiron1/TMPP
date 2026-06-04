package com.mihai.library.visitor;

import com.mihai.library.domain.Book;
import com.mihai.library.domain.Dvd;
import com.mihai.library.domain.LibraryItem;
import com.mihai.library.domain.LibraryItemGroup;
import com.mihai.library.domain.Magazine;

import java.util.stream.Collectors;

public final class CatalogDescriptionVisitor implements LibraryItemVisitor<String> {
    @Override
    public String visitBook(Book book) {
        return "BOOK " + book.getId() + " - " + book.getTitle() +
                " by " + book.getAuthor() + " (ISBN " + book.getIsbn() + ")";
    }

    @Override
    public String visitMagazine(Magazine magazine) {
        return "MAGAZINE " + magazine.getId() + " - " + magazine.getTitle() +
                " issue " + magazine.getIssueNumber();
    }

    @Override
    public String visitDvd(Dvd dvd) {
        return "DVD " + dvd.getId() + " - " + dvd.getTitle() +
                " (" + dvd.getDurationMinutes() + " min)";
    }

    @Override
    public String visitGroup(LibraryItemGroup group) {
        String childDescriptions = group.getChildren().stream()
                .map(this::describeChild)
                .collect(Collectors.joining("; "));
        return "GROUP " + group.getId() + " - " + group.getTitle() +
                " contains [" + childDescriptions + "]";
    }

    private String describeChild(LibraryItem item) {
        return item.accept(this);
    }
}
