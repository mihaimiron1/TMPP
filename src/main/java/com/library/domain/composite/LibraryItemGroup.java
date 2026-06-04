package com.library.domain.composite;

import com.library.domain.model.CatalogComponent;
import com.library.domain.model.LibraryItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// PATTERN: Composite — composite node that can hold leaves (LibraryItem) or other groups,
//          enabling uniform treatment of individual items and collections
public class LibraryItemGroup implements CatalogComponent {

    private final String id;
    private final String title;
    private final String description;
    private final List<CatalogComponent> children = new ArrayList<>();

    public LibraryItemGroup(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public void add(CatalogComponent component) {
        children.add(component);
    }

    public void remove(CatalogComponent component) {
        children.remove(component);
    }

    public List<CatalogComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }

    // Recursively collect every leaf LibraryItem in this subtree
    public List<LibraryItem> getAllItems() {
        List<LibraryItem> items = new ArrayList<>();
        for (CatalogComponent child : children) {
            if (child.isLeaf()) {
                items.add((LibraryItem) child);
            } else {
                items.addAll(((LibraryItemGroup) child).getAllItems());
            }
        }
        return items;
    }

    public long getAvailableCount() {
        return getAllItems().stream().filter(LibraryItem::isAvailable).count();
    }

    public int getTotalCount() {
        return getAllItems().size();
    }

    @Override
    public String getId()          { return id; }

    @Override
    public String getTitle()       { return title; }

    @Override
    public String getDescription() { return description + " (" + getTotalCount() + " items, " + getAvailableCount() + " available)"; }

    @Override
    public boolean isLeaf()        { return false; }
}
