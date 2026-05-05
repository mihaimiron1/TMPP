package com.mihai.library.visitor;

import com.mihai.library.domain.Book;
import com.mihai.library.domain.Dvd;
import com.mihai.library.domain.LibraryItemGroup;
import com.mihai.library.domain.Magazine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CatalogVisitorTest {
    @Test
    void descriptionVisitor_formatsConcreteItemDetails() {
        CatalogDescriptionVisitor visitor = new CatalogDescriptionVisitor();

        String description = Book.builder()
                .id("B1")
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .build()
                .accept(visitor);

        assertEquals("BOOK B1 - Clean Code by Robert C. Martin (ISBN 978-0132350884)", description);
    }

    @Test
    void descriptionVisitor_traversesCompositeChildren() {
        LibraryItemGroup group = LibraryItemGroup.builder()
                .id("G1")
                .title("Starter Bundle")
                .child(Book.builder()
                        .id("B1")
                        .title("Clean Code")
                        .author("Robert C. Martin")
                        .isbn("978-0132350884")
                        .build())
                .child(Magazine.builder()
                        .id("M1")
                        .title("National Geographic")
                        .issueNumber(202)
                        .build())
                .build();

        String description = group.accept(new CatalogDescriptionVisitor());

        assertTrue(description.contains("GROUP G1 - Starter Bundle contains"));
        assertTrue(description.contains("BOOK B1 - Clean Code"));
        assertTrue(description.contains("MAGAZINE M1 - National Geographic issue 202"));
    }

    @Test
    void statisticsVisitor_countsVisitedItemTypes() {
        CatalogStatisticsVisitor visitor = new CatalogStatisticsVisitor();

        Book.builder()
                .id("B1")
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .build()
                .accept(visitor);
        Magazine.builder()
                .id("M1")
                .title("National Geographic")
                .issueNumber(202)
                .build()
                .accept(visitor);
        Dvd.builder()
                .id("D1")
                .title("Interstellar")
                .durationMinutes(169)
                .build()
                .accept(visitor);
        LibraryItemGroup.builder()
                .id("G1")
                .title("Starter Bundle")
                .child(Book.builder()
                        .id("B2")
                        .title("Effective Java")
                        .author("Joshua Bloch")
                        .isbn("978-0134685991")
                        .build())
                .build()
                .accept(visitor);

        CatalogStatistics statistics = visitor.statistics();

        assertEquals(1, statistics.getBooks());
        assertEquals(1, statistics.getMagazines());
        assertEquals(1, statistics.getDvds());
        assertEquals(1, statistics.getGroups());
        assertEquals(4, statistics.getTotalItems());
    }
}
