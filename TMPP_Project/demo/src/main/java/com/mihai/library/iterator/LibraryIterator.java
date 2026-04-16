package com.mihai.library.iterator;

public interface LibraryIterator<T> {
    boolean hasNext();

    T next();
}
