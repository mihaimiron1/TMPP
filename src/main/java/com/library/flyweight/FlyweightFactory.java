package com.library.flyweight;

import java.util.concurrent.ConcurrentHashMap;

// PATTERN: Flyweight — thread-safe pool; computeIfAbsent guarantees at most one instance per key
public final class FlyweightFactory {

    private static final ConcurrentHashMap<String, AuthorFlyweight> AUTHORS = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, GenreFlyweight>  GENRES  = new ConcurrentHashMap<>();

    public static AuthorFlyweight getAuthor(String name) {
        return AUTHORS.computeIfAbsent(name.trim(), AuthorFlyweight::new);
    }

    public static GenreFlyweight getGenre(String name) {
        return GENRES.computeIfAbsent(name.trim(), GenreFlyweight::new);
    }

    public static int getAuthorPoolSize() { return AUTHORS.size(); }
    public static int getGenrePoolSize()  { return GENRES.size(); }

    private FlyweightFactory() {}
}
