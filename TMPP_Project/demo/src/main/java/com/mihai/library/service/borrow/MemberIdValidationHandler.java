package com.mihai.library.service.borrow;

public final class MemberIdValidationHandler extends BaseBorrowHandler {
    @Override
    public void handle(BorrowRequestContext context) {
        if (context == null) {
            throw new IllegalArgumentException("context null");
        }
        if (context.getMemberId() == null || context.getMemberId().isBlank()) {
            throw new IllegalArgumentException("memberId invalid");
        }
        next(context);
    }
}
