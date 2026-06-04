package com.library.infrastructure.excel.mapper;

import com.library.domain.model.Magazine;
import com.library.infrastructure.excel.CellUtils;
import com.library.infrastructure.excel.RowMapper;
import org.apache.poi.ss.usermodel.Row;

// PATTERN: Adapter (concrete) — adapts Apache POI Row to/from Magazine domain object
public class MagazineRowMapper implements RowMapper<Magazine> {

    @Override
    public Magazine fromRow(Row row) {
        return new Magazine(
            CellUtils.getString(row, 0),
            CellUtils.getString(row, 1),
            CellUtils.getString(row, 2),
            CellUtils.getInt   (row, 3),
            CellUtils.getString(row, 4),
            CellUtils.getString(row, 5),
            CellUtils.getInt   (row, 6),
            CellUtils.getInt   (row, 7),
            CellUtils.getInt   (row, 8),
            CellUtils.getDouble(row, 9)
        );
    }

    @Override
    public void writeToRow(Magazine mag, Row row) {
        CellUtils.setString(row, 0, mag.getId());
        CellUtils.setString(row, 1, mag.getTitle());
        CellUtils.setString(row, 2, mag.getPublisher());
        CellUtils.setInt   (row, 3, mag.getIssueNumber());
        CellUtils.setString(row, 4, mag.getMonth());
        CellUtils.setString(row, 5, mag.getGenre());
        CellUtils.setInt   (row, 6, mag.getYear());
        CellUtils.setInt   (row, 7, mag.getQuantity());
        CellUtils.setInt   (row, 8, mag.getAvailableQuantity());
        CellUtils.setDouble(row, 9, mag.getDailyPenaltyMDL());
    }
}
