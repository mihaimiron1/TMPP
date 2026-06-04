package com.library.infrastructure.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// PATTERN: Singleton — single shared access point to the Excel workbook; ensures no concurrent file conflicts
public class ExcelDatabaseManager {

    private static volatile ExcelDatabaseManager instance;

    private final String filePath;
    private Workbook workbook;

    private ExcelDatabaseManager(String filePath) {
        this.filePath = filePath;
        load();
    }

    public static ExcelDatabaseManager getInstance(String filePath) {
        if (instance == null) {
            synchronized (ExcelDatabaseManager.class) {
                if (instance == null) {
                    instance = new ExcelDatabaseManager(filePath);
                }
            }
        }
        return instance;
    }

    private void load() {
        Path path = Paths.get(filePath);
        try {
            if (Files.exists(path)) {
                try (InputStream is = Files.newInputStream(path)) {
                    workbook = new XSSFWorkbook(is);
                }
            } else {
                workbook = new XSSFWorkbook();
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load Excel file: " + filePath, e);
        }
    }

    public Sheet getSheet(String name) {
        return workbook.getSheet(name);
    }

    public Sheet getOrCreateSheet(String name) {
        Sheet sheet = workbook.getSheet(name);
        return sheet != null ? sheet : workbook.createSheet(name);
    }

    public synchronized void save() {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            try (OutputStream os = Files.newOutputStream(path)) {
                workbook.write(os);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot save Excel file: " + filePath, e);
        }
    }

    public Workbook getWorkbook() {
        return workbook;
    }
}
