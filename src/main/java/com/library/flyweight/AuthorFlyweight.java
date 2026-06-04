package com.library.flyweight;

// PATTERN: Flyweight — immutable intrinsic state shared across all Book objects with the same author
public final class AuthorFlyweight {

    private final String name;

    // Package-private: only FlyweightFactory creates instances
    AuthorFlyweight(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    @Override
    public String toString() { return name; }
}
