package com.mihai.library.domain;

public final class Member {
    private final String id;
    private final String name;

    public Member(String id, String name) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id invalid");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name invalid");
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "Member{id='" + id + "', name='" + name + "'}";
    }
}