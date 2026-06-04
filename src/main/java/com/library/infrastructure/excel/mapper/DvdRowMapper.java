package com.library.infrastructure.excel.mapper;

import com.library.domain.model.Dvd;
import com.library.infrastructure.excel.CellUtils;
import com.library.infrastructure.excel.RowMapper;
import org.apache.poi.ss.usermodel.Row;

// PATTERN: Adapter (concrete) — adapts Apache POI Row to/from Dvd domain object
public class DvdRowMapper implements RowMapper<Dvd> {

    @Override
    public Dvd fromRow(Row row) {
        return new Dvd(
            CellUtils.getString(row, 0),
            CellUtils.getString(row, 1),
            CellUtils.getString(row, 2),
            CellUtils.getString(row, 3),
            CellUtils.getInt   (row, 4),
            CellUtils.getInt   (row, 5),
            CellUtils.getInt   (row, 6),
            CellUtils.getInt   (row, 7),
            CellUtils.getDouble(row, 8)
        );
    }

    @Override
    public void writeToRow(Dvd dvd, Row row) {
        CellUtils.setString(row, 0, dvd.getId());
        CellUtils.setString(row, 1, dvd.getTitle());
        CellUtils.setString(row, 2, dvd.getDirector());
        CellUtils.setString(row, 3, dvd.getGenre());
        CellUtils.setInt   (row, 4, dvd.getDurationMinutes());
        CellUtils.setInt   (row, 5, dvd.getYear());
        CellUtils.setInt   (row, 6, dvd.getQuantity());
        CellUtils.setInt   (row, 7, dvd.getAvailableQuantity());
        CellUtils.setDouble(row, 8, dvd.getDailyPenaltyMDL());
    }
}
