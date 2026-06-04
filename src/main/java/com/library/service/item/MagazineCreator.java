package com.library.service.item;

import com.library.domain.model.LibraryItem;
import com.library.domain.model.Magazine;

// PATTERN: Factory Method (concrete creator) — instantiates Magazine with magazine-specific fields
public class MagazineCreator extends LibraryItemCreator {

    @Override
    protected LibraryItem createItem(ItemRequest request) {
        double penalty = request.getDailyPenaltyMDL() > 0
            ? request.getDailyPenaltyMDL()
            : Magazine.DEFAULT_PENALTY_MDL;
        return new Magazine(
            null,
            request.getTitle(),
            request.getPublisher(),
            request.getIssueNumber(),
            request.getMonth(),
            request.getGenre(),
            request.getYear(),
            request.getQuantity(),
            request.getQuantity(),
            penalty
        );
    }

    @Override
    protected void validate(ItemRequest request) {
        super.validate(request);
        if (request.getPublisher() == null || request.getPublisher().isBlank()) {
            throw new IllegalArgumentException("Publisher is required for magazines");
        }
    }
}
