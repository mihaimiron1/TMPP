package com.library.infrastructure.excel;

import org.apache.poi.ss.usermodel.Row;

// PATTERN: Adapter — defines the contract for converting between Apache POI Row and domain objects
public interface RowMapper<T> {
    T fromRow(Row row);
    void writeToRow(T entity, Row row);
}
