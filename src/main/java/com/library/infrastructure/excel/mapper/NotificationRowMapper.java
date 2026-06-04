package com.library.infrastructure.excel.mapper;

import com.library.domain.enums.NotificationType;
import com.library.domain.model.Notification;
import com.library.infrastructure.excel.CellUtils;
import com.library.infrastructure.excel.RowMapper;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

// PATTERN: Adapter (concrete) — adapts Apache POI Row to/from Notification domain object
public class NotificationRowMapper implements RowMapper<Notification> {

    @Override
    public Notification fromRow(Row row) {
        Notification n = new Notification();
        n.setId(CellUtils.getString(row, 0));
        n.setUserId(CellUtils.getString(row, 1));
        n.setMessage(CellUtils.getString(row, 2));
        n.setType(parseType(CellUtils.getString(row, 3)));
        n.setCreatedAt(parseDate(CellUtils.getString(row, 4)));
        n.setRead(CellUtils.getBoolean(row, 5));
        return n;
    }

    @Override
    public void writeToRow(Notification n, Row row) {
        CellUtils.setString (row, 0, n.getId());
        CellUtils.setString (row, 1, n.getUserId());
        CellUtils.setString (row, 2, n.getMessage());
        CellUtils.setString (row, 3, n.getType().name());
        CellUtils.setString (row, 4, n.getCreatedAt().toString());
        CellUtils.setBoolean(row, 5, n.isRead());
    }

    private LocalDate parseDate(String s) {
        try { return LocalDate.parse(s); }
        catch (DateTimeParseException e) { return LocalDate.now(); }
    }

    private NotificationType parseType(String s) {
        try { return NotificationType.valueOf(s); }
        catch (IllegalArgumentException e) { return NotificationType.DUE_SOON; }
    }
}
