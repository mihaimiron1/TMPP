package com.library.infrastructure.excel;

import com.library.domain.model.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder implements ApplicationRunner {

    @Value("${library.excel.path}")
    private String excelPath;

    @Override
    public void run(ApplicationArguments args) {
        ExcelDatabaseManager db = ExcelDatabaseManager.getInstance(excelPath);
        if (alreadySeeded(db)) return;

        createHeaders(db);
        seedUsers(db);
        seedBooks(db);
        seedMagazines(db);
        seedDvds(db);
        db.save();
    }

    private boolean alreadySeeded(ExcelDatabaseManager db) {
        Sheet sheet = db.getSheet(ExcelSheetConstants.USERS);
        return sheet != null && sheet.getLastRowNum() >= 1;
    }

    // ── Headers ─────────────────────────────────────────────────────────────

    private void createHeaders(ExcelDatabaseManager db) {
        addHeader(db.getOrCreateSheet(ExcelSheetConstants.USERS),
            "id", "email", "password", "name", "registeredAt");
        addHeader(db.getOrCreateSheet(ExcelSheetConstants.BOOKS),
            "id", "title", "author", "isbn", "genre", "year", "quantity", "availableQuantity", "dailyPenaltyMDL");
        addHeader(db.getOrCreateSheet(ExcelSheetConstants.MAGAZINES),
            "id", "title", "publisher", "issueNumber", "month", "genre", "year", "quantity", "availableQuantity", "dailyPenaltyMDL");
        addHeader(db.getOrCreateSheet(ExcelSheetConstants.DVDS),
            "id", "title", "director", "genre", "durationMinutes", "year", "quantity", "availableQuantity", "dailyPenaltyMDL");
        addHeader(db.getOrCreateSheet(ExcelSheetConstants.LOANS),
            "id", "userId", "itemId", "itemType", "borrowDate", "dueDate", "returnDate", "status", "penaltyMDL");
        addHeader(db.getOrCreateSheet(ExcelSheetConstants.NOTIFICATIONS),
            "id", "userId", "message", "type", "createdAt", "isRead");
    }

    private void addHeader(Sheet sheet, String... columns) {
        Row header = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            CellUtils.setString(header, i, columns[i]);
        }
    }

    // ── Seed data ────────────────────────────────────────────────────────────

    private void seedUsers(ExcelDatabaseManager db) {
        Sheet sheet = db.getSheet(ExcelSheetConstants.USERS);
        writeUser(sheet, 1, "user-001", "demo@library.com", "demo123", "Demo User", "2024-01-15");
    }

    private void seedBooks(ExcelDatabaseManager db) {
        Sheet sheet = db.getSheet(ExcelSheetConstants.BOOKS);
        writeBook(sheet, 1, "book-001", "The Great Gatsby",  "F. Scott Fitzgerald", "978-0743273565", "Fiction",    1925, 3, 3, 1.5);
        writeBook(sheet, 2, "book-002", "1984",              "George Orwell",        "978-0451524935", "Dystopia",   1949, 2, 2, 1.5);
        writeBook(sheet, 3, "book-003", "Dune",              "Frank Herbert",        "978-0441013593", "Sci-Fi",     1965, 2, 2, 1.5);
        writeBook(sheet, 4, "book-004", "Clean Code",        "Robert C. Martin",     "978-0132350884", "Technology", 2008, 1, 1, 1.5);
        writeBook(sheet, 5, "book-005", "The Hobbit",        "J.R.R. Tolkien",       "978-0618260300", "Fantasy",    1937, 3, 3, 1.5);
    }

    private void seedMagazines(ExcelDatabaseManager db) {
        Sheet sheet = db.getSheet(ExcelSheetConstants.MAGAZINES);
        writeMagazine(sheet, 1, "mag-001", "National Geographic",  "Nat Geo Partners", 5, "May",    "Science", 2024, 2, 2, 0.5);
        writeMagazine(sheet, 2, "mag-002", "Scientific American",  "Springer Nature",  3, "March",  "Science", 2024, 1, 1, 0.5);
        writeMagazine(sheet, 3, "mag-003", "Time",                 "Time USA",         8, "August", "News",    2024, 2, 2, 0.5);
    }

    private void seedDvds(ExcelDatabaseManager db) {
        Sheet sheet = db.getSheet(ExcelSheetConstants.DVDS);
        writeDvd(sheet, 1, "dvd-001", "Inception",     "Christopher Nolan",  "Sci-Fi",  148, 2010, 2, 2, 2.0);
        writeDvd(sheet, 2, "dvd-002", "The Matrix",    "The Wachowskis",     "Sci-Fi",  136, 1999, 1, 1, 2.0);
        writeDvd(sheet, 3, "dvd-003", "Interstellar",  "Christopher Nolan",  "Sci-Fi",  169, 2014, 2, 2, 2.0);
    }

    // ── Row writers ──────────────────────────────────────────────────────────

    private void writeUser(Sheet sheet, int rowIdx, String id, String email, String password, String name, String date) {
        Row row = sheet.createRow(rowIdx);
        CellUtils.setString(row, 0, id);
        CellUtils.setString(row, 1, email);
        CellUtils.setString(row, 2, password);
        CellUtils.setString(row, 3, name);
        CellUtils.setString(row, 4, date);
    }

    private void writeBook(Sheet sheet, int rowIdx, String id, String title, String author,
                           String isbn, String genre, int year, int qty, int avail, double penalty) {
        Row row = sheet.createRow(rowIdx);
        CellUtils.setString(row, 0, id);
        CellUtils.setString(row, 1, title);
        CellUtils.setString(row, 2, author);
        CellUtils.setString(row, 3, isbn);
        CellUtils.setString(row, 4, genre);
        CellUtils.setInt   (row, 5, year);
        CellUtils.setInt   (row, 6, qty);
        CellUtils.setInt   (row, 7, avail);
        CellUtils.setDouble(row, 8, penalty);
    }

    private void writeMagazine(Sheet sheet, int rowIdx, String id, String title, String publisher,
                               int issue, String month, String genre, int year,
                               int qty, int avail, double penalty) {
        Row row = sheet.createRow(rowIdx);
        CellUtils.setString(row, 0, id);
        CellUtils.setString(row, 1, title);
        CellUtils.setString(row, 2, publisher);
        CellUtils.setInt   (row, 3, issue);
        CellUtils.setString(row, 4, month);
        CellUtils.setString(row, 5, genre);
        CellUtils.setInt   (row, 6, year);
        CellUtils.setInt   (row, 7, qty);
        CellUtils.setInt   (row, 8, avail);
        CellUtils.setDouble(row, 9, penalty);
    }

    private void writeDvd(Sheet sheet, int rowIdx, String id, String title, String director,
                          String genre, int duration, int year, int qty, int avail, double penalty) {
        Row row = sheet.createRow(rowIdx);
        CellUtils.setString(row, 0, id);
        CellUtils.setString(row, 1, title);
        CellUtils.setString(row, 2, director);
        CellUtils.setString(row, 3, genre);
        CellUtils.setInt   (row, 4, duration);
        CellUtils.setInt   (row, 5, year);
        CellUtils.setInt   (row, 6, qty);
        CellUtils.setInt   (row, 7, avail);
        CellUtils.setDouble(row, 8, penalty);
    }
}
