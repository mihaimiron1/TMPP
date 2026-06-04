package com.library.web;

import com.library.facade.LibraryFacade;
import com.library.web.dto.LoginRequest;
import com.library.web.dto.LoginResponse;
import com.library.web.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LibraryFacade facade;

    public AuthController(LibraryFacade facade) {
        this.facade = facade;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(facade.login(req.getEmail(), req.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(facade.register(req.getEmail(), req.getPassword(), req.getName()));
    }
}
