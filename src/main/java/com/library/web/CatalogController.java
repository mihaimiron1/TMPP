package com.library.web;

import com.library.domain.enums.ItemType;
import com.library.domain.model.LibraryItem;
import com.library.facade.LibraryFacade;
import com.library.visitor.ItemSummaryVisitor;
import com.library.visitor.VisitorDispatcher;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final LibraryFacade facade;

    public CatalogController(LibraryFacade facade) {
        this.facade = facade;
    }

    // PATTERN: Visitor — ItemSummaryVisitor adds type-specific fields without instanceof chains
    @GetMapping
    public List<Map<String, Object>> catalog(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) ItemType type,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "false") boolean availableOnly) {

        boolean hasFilter = query != null || type != null || genre != null || availableOnly;
        List<LibraryItem> items = hasFilter
            ? facade.search(userId != null ? userId : "anonymous", query, type, genre, availableOnly)
            : facade.getAllItems();

        return toResponse(items);
    }

    @GetMapping("/undo-search")
    public List<Map<String, Object>> undoSearch(
            @RequestHeader("X-User-Id") String userId) {
        return toResponse(facade.undoSearch(userId));
    }

    private List<Map<String, Object>> toResponse(List<LibraryItem> items) {
        ItemSummaryVisitor visitor = new ItemSummaryVisitor();
        return items.stream().map(item -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id",                item.getId());
            m.put("title",             item.getTitle());
            m.put("type",              item.getType());
            m.put("genre",             item.getGenre());
            m.put("year",              item.getYear());
            m.put("quantity",          item.getQuantity());
            m.put("availableQuantity", item.getAvailableQuantity());
            m.put("available",         item.isAvailable());
            m.put("description",       item.getDescription());
            m.putAll(VisitorDispatcher.dispatch(item, visitor));
            return m;
        }).toList();
    }
}
