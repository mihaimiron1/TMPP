package com.library.service.item;

import com.library.domain.model.Dvd;
import com.library.domain.model.LibraryItem;

// PATTERN: Factory Method (concrete creator) — instantiates Dvd with DVD-specific fields
public class DvdCreator extends LibraryItemCreator {

    @Override
    protected LibraryItem createItem(ItemRequest request) {
        double penalty = request.getDailyPenaltyMDL() > 0
            ? request.getDailyPenaltyMDL()
            : Dvd.DEFAULT_PENALTY_MDL;
        return new Dvd(
            null,
            request.getTitle(),
            request.getDirector(),
            request.getGenre(),
            request.getDurationMinutes(),
            request.getYear(),
            request.getQuantity(),
            request.getQuantity(),
            penalty
        );
    }

    @Override
    protected void validate(ItemRequest request) {
        super.validate(request);
        if (request.getDirector() == null || request.getDirector().isBlank()) {
            throw new IllegalArgumentException("Director is required for DVDs");
        }
    }
}
