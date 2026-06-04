package com.library.infrastructure.excel.mapper;

import com.library.domain.model.Book;
import com.library.infrastructure.excel.CellUtils;
import com.library.infrastructure.excel.RowMapper;
import org.apache.poi.ss.usermodel.Row;

// PATTERN: Adapter (concrete) — adapts Apache POI Row to/from Book domain object
public class BookRowMapper implements RowMapper<Book> {

    @Override
    public Book fromRow(Row row) {
        return new Book(
            CellUtils.getString(row, 0),
            CellUtils.getString(row, 1),
            CellUtils.getString(row, 2),
            CellUtils.getString(row, 3),
            CellUtils.getString(row, 4),
            CellUtils.getInt(row, 5),
            CellUtils.getInt(row, 6),
            CellUtils.getInt(row, 7),
            CellUtils.getDouble(row, 8)
        );
    }

    @Override
    public void writeToRow(Book book, Row row) {
        CellUtils.setString(row, 0, book.getId());
        CellUtils.setString(row, 1, book.getTitle());
        CellUtils.setString(row, 2, book.getAuthor());
        CellUtils.setString(row, 3, book.getIsbn());
        CellUtils.setString(row, 4, book.getGenre());
        CellUtils.setInt   (row, 5, book.getYear());
        CellUtils.setInt   (row, 6, book.getQuantity());
        CellUtils.setInt   (row, 7, book.getAvailableQuantity());
        CellUtils.setDouble(row, 8, book.getDailyPenaltyMDL());
    }
}
