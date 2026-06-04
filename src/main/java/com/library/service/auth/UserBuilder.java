package com.library.service.auth;

import com.library.domain.model.User;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

// PATTERN: Builder — encapsulates User construction; separates validation from object creation
public class UserBuilder {

    private String id            = UUID.randomUUID().toString();
    private String email;
    private String password;
    private String name;
    private LocalDate registeredAt = LocalDate.now();

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder registeredAt(LocalDate date) {
        this.registeredAt = date;
        return this;
    }

    public User build() {
        Objects.requireNonNull(email,    "email is required");
        Objects.requireNonNull(password, "password is required");
        Objects.requireNonNull(name,     "name is required");
        if (email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters");
        }
        return new User(id, email.trim().toLowerCase(), password, name.trim(), registeredAt);
    }
}
