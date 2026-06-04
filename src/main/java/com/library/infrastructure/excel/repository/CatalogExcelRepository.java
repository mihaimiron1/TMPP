package com.library.infrastructure.excel.repository;

import com.library.domain.enums.ItemType;
import com.library.domain.model.Book;
import com.library.domain.model.Dvd;
import com.library.domain.model.LibraryItem;
import com.library.domain.model.Magazine;
import com.library.domain.repository.CatalogRepository;
import com.library.infrastructure.excel.CellUtils;
import com.library.infrastructure.excel.ExcelDatabaseManager;
import com.library.infrastructure.excel.ExcelSheetConstants;
import com.library.infrastructure.excel.mapper.BookRowMapper;
import com.library.infrastructure.excel.mapper.DvdRowMapper;
import com.library.infrastructure.excel.mapper.MagazineRowMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CatalogExcelRepository implements CatalogRepository {

    private final ExcelDatabaseManager db;
    private final BookRowMapper bookMapper       = new BookRowMapper();
    private final MagazineRowMapper magMapper    = new MagazineRowMapper();
    private final DvdRowMapper dvdMapper         = new DvdRowMapper();

    public CatalogExcelRepository(@Value("${library.excel.path}") String excelPath) {
        this.db = ExcelDatabaseManager.getInstance(excelPath);
    }

    @Override
    public List<Book> findAllBooks() {
        List<Book> books = new ArrayList<>();
        Sheet sheet = db.getSheet(ExcelSheetConstants.BOOKS);
        if (sheet == null) return books;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) books.add(bookMapper.fromRow(row));
        }
        return books;
    }

    @Override
    public List<Magazine> findAllMagazines() {
        List<Magazine> mags = new ArrayList<>();
        Sheet sheet = db.getSheet(ExcelSheetConstants.MAGAZINES);
        if (sheet == null) return mags;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) mags.add(magMapper.fromRow(row));
        }
        return mags;
    }

    @Override
    public List<Dvd> findAllDvds() {
        List<Dvd> dvds = new ArrayList<>();
        Sheet sheet = db.getSheet(ExcelSheetConstants.DVDS);
        if (sheet == null) return dvds;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) dvds.add(dvdMapper.fromRow(row));
        }
        return dvds;
    }

    @Override
    public List<LibraryItem> findAll() {
        List<LibraryItem> all = new ArrayList<>();
        all.addAll(findAllBooks());
        all.addAll(findAllMagazines());
        all.addAll(findAllDvds());
        return all;
    }

    @Override
    public Optional<LibraryItem> findById(String id, ItemType type) {
        return switch (type) {
            case BOOK     -> findAllBooks().stream().filter(b -> id.equals(b.getId())).map(b -> (LibraryItem) b).findFirst();
            case MAGAZINE -> findAllMagazines().stream().filter(m -> id.equals(m.getId())).map(m -> (LibraryItem) m).findFirst();
            case DVD      -> findAllDvds().stream().filter(d -> id.equals(d.getId())).map(d -> (LibraryItem) d).findFirst();
        };
    }

    @Override
    public void updateAvailableQuantity(String id, ItemType type, int newAvailableQuantity) {
        String sheetName = switch (type) {
            case BOOK     -> ExcelSheetConstants.BOOKS;
            case MAGAZINE -> ExcelSheetConstants.MAGAZINES;
            case DVD      -> ExcelSheetConstants.DVDS;
        };
        int idCol = 0;
        int availCol = switch (type) {
            case BOOK, DVD -> ExcelSheetConstants.BOOK_COL_AVAILABLE;
            case MAGAZINE  -> ExcelSheetConstants.MAG_COL_AVAILABLE;
        };
        Sheet sheet = db.getSheet(sheetName);
        if (sheet == null) return;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            if (id.equals(CellUtils.getString(row, idCol))) {
                CellUtils.setInt(row, availCol, newAvailableQuantity);
                db.save();
                return;
            }
        }
    }
}
