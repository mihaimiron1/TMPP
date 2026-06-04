package com.library.visitor;

import com.library.domain.model.Book;
import com.library.domain.model.Dvd;
import com.library.domain.model.Magazine;

import java.util.LinkedHashMap;
import java.util.Map;

// PATTERN: Visitor (concrete) — builds type-specific detail maps for the catalog API response;
//          avoids instanceof chains in the controller layer
public class ItemSummaryVisitor implements LibraryItemVisitor<Map<String, Object>> {

    @Override
    public Map<String, Object> visit(Book book) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("author",      book.getAuthor());
        m.put("isbn",        book.getIsbn());
        m.put("loanDays",    Book.DEFAULT_LOAN_DAYS);
        m.put("penaltyMDL",  book.getDailyPenaltyMDL());
        return m;
    }

    @Override
    public Map<String, Object> visit(Magazine mag) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("publisher",   mag.getPublisher());
        m.put("issueNumber", mag.getIssueNumber());
        m.put("month",       mag.getMonth());
        m.put("loanDays",    Magazine.DEFAULT_LOAN_DAYS);
        m.put("penaltyMDL",  mag.getDailyPenaltyMDL());
        return m;
    }

    @Override
    public Map<String, Object> visit(Dvd dvd) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("director",         dvd.getDirector());
        m.put("durationMinutes",  dvd.getDurationMinutes());
        m.put("loanDays",         Dvd.DEFAULT_LOAN_DAYS);
        m.put("penaltyMDL",       dvd.getDailyPenaltyMDL());
        return m;
    }
}
