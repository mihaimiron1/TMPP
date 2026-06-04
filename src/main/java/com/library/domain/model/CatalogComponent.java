package com.library.domain.model;

// PATTERN: Composite — common interface for both leaf items (Book, Magazine, Dvd)
//          and composite nodes (LibraryItemGroup), making them interchangeable in the catalog tree
public interface CatalogComponent {
    String getId();
    String getTitle();
    String getDescription();
    boolean isLeaf();
}
