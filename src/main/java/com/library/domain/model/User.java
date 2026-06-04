package com.library.domain.model;

import java.time.LocalDate;

public class User {

    private String id;
    private String email;
    private String password;
    private String name;
    private LocalDate registeredAt;

    public User() {}

    public User(String id, String email, String password, String name, LocalDate registeredAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.registeredAt = registeredAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDate registeredAt) { this.registeredAt = registeredAt; }
}
