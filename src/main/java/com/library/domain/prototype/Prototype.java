package com.library.domain.prototype;

// PATTERN: Prototype — contract for creating copies of domain objects without coupling to their concrete type
public interface Prototype<T> {
    T clone();
}
