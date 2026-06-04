package com.library.infrastructure.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public final class CellUtils {

    private CellUtils() {}

    public static String getString(Row row, int col) {
        Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default      -> "";
        };
    }

    public static int getInt(Row row, int col) {
        Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return 0;
        return switch (cell.getCellType()) {
            case NUMERIC -> (int) cell.getNumericCellValue();
            case STRING  -> {
                try { yield Integer.parseInt(cell.getStringCellValue().trim()); }
                catch (NumberFormatException e) { yield 0; }
            }
            default -> 0;
        };
    }

    public static double getDouble(Row row, int col) {
        Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return 0.0;
        return switch (cell.getCellType()) {
            case NUMERIC -> cell.getNumericCellValue();
            case STRING  -> {
                try { yield Double.parseDouble(cell.getStringCellValue().trim()); }
                catch (NumberFormatException e) { yield 0.0; }
            }
            default -> 0.0;
        };
    }

    public static boolean getBoolean(Row row, int col) {
        Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return false;
        return switch (cell.getCellType()) {
            case BOOLEAN -> cell.getBooleanCellValue();
            case STRING  -> Boolean.parseBoolean(cell.getStringCellValue().trim());
            default      -> false;
        };
    }

    // ── Date helpers ──────────────────────────────────────────────────────────

    /**
     * Reads a date cell. Handles both Excel Date (NUMERIC formatted) and
     * legacy ISO-string cells (backward-compatible fallback).
     */
    public static LocalDate getDateCell(Row row, int col) {
        Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;
        if (cell.getCellType() == org.apache.poi.ss.usermodel.CellType.NUMERIC
                && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }
        // Fallback: legacy string format "yyyy-MM-dd"
        if (cell.getCellType() == org.apache.poi.ss.usermodel.CellType.STRING) {
            String s = cell.getStringCellValue().trim();
            if (s.isEmpty()) return null;
            try { return LocalDate.parse(s); } catch (Exception ignored) {}
        }
        return null;
    }

    /**
     * Writes a LocalDate as a true Excel Date cell with "yyyy-mm-dd" display format.
     * The workbook reference is obtained from row.getSheet().getWorkbook().
     */
    public static void setDateCell(Row row, int col, LocalDate date) {
        Cell cell = row.createCell(col);
        if (date == null) {
            cell.setCellValue("");
            return;
        }
        Date javaDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        cell.setCellValue(javaDate);
        // Apply date display format
        Workbook wb = row.getSheet().getWorkbook();
        CellStyle style = wb.createCellStyle();
        style.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-mm-dd"));
        cell.setCellStyle(style);
    }

    // ── Primitive setters ─────────────────────────────────────────────────────

    public static void setString(Row row, int col, String value) {
        row.createCell(col).setCellValue(value != null ? value : "");
    }

    public static void setInt(Row row, int col, int value) {
        row.createCell(col).setCellValue(value);
    }

    public static void setDouble(Row row, int col, double value) {
        row.createCell(col).setCellValue(value);
    }

    public static void setBoolean(Row row, int col, boolean value) {
        row.createCell(col).setCellValue(String.valueOf(value));
    }
}
