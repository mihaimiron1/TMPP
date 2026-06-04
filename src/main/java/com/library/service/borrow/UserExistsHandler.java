package com.library.service.borrow;

import com.library.domain.repository.UserRepository;

// PATTERN: Chain of Responsibility (handler 1/5) — verifies the requesting user exists; loads User into context
public class UserExistsHandler extends BorrowHandler {

    private final UserRepository userRepo;

    public UserExistsHandler(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void handle(BorrowContext ctx) {
        userRepo.findById(ctx.getUserId())
            .ifPresentOrElse(
                ctx::setUser,
                () -> { throw new RuntimeException("User not found: " + ctx.getUserId()); }
            );
        passToNext(ctx);
    }
}
