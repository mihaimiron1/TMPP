package com.library.service.auth;

import com.library.domain.model.User;
import com.library.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User login(String email, String password) {
        return userRepo.findByEmail(email)
            .filter(u -> u.getPassword().equals(password))
            .orElseThrow(() -> new RuntimeException("Invalid email or password"));
    }

    public User register(String email, String password, String name) {
        if (userRepo.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered: " + email);
        }
        User user = new UserBuilder()
            .email(email)
            .password(password)
            .name(name)
            .build();
        return userRepo.save(user);
    }
}
