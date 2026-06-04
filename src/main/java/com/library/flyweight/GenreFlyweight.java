package com.library.flyweight;

// PATTERN: Flyweight — immutable intrinsic state shared across all LibraryItems of the same genre
public final class GenreFlyweight {

    private final String name;

    GenreFlyweight(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    @Override
    public String toString() { return name; }
}
