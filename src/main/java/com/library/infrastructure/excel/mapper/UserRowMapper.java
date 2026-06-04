package com.library.infrastructure.excel.mapper;

import com.library.domain.model.User;
import com.library.infrastructure.excel.CellUtils;
import com.library.infrastructure.excel.RowMapper;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

// PATTERN: Adapter (concrete) — adapts Apache POI Row to/from User domain object
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User fromRow(Row row) {
        String dateStr = CellUtils.getString(row, 4);
        LocalDate registeredAt;
        try {
            registeredAt = LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            registeredAt = LocalDate.now();
        }
        return new User(
            CellUtils.getString(row, 0),
            CellUtils.getString(row, 1),
            CellUtils.getString(row, 2),
            CellUtils.getString(row, 3),
            registeredAt
        );
    }

    @Override
    public void writeToRow(User user, Row row) {
        CellUtils.setString(row, 0, user.getId());
        CellUtils.setString(row, 1, user.getEmail());
        CellUtils.setString(row, 2, user.getPassword());
        CellUtils.setString(row, 3, user.getName());
        CellUtils.setString(row, 4, user.getRegisteredAt().toString());
    }
}
