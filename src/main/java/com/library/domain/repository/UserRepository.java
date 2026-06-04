package com.library.domain.repository;

import com.library.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);
    User save(User user);
}
