package com.library.infrastructure.excel.repository;

import com.library.domain.model.User;
import com.library.domain.repository.UserRepository;
import com.library.infrastructure.excel.ExcelDatabaseManager;
import com.library.infrastructure.excel.ExcelSheetConstants;
import com.library.infrastructure.excel.mapper.UserRowMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserExcelRepository implements UserRepository {

    private final ExcelDatabaseManager db;
    private final UserRowMapper mapper = new UserRowMapper();

    public UserExcelRepository(@Value("${library.excel.path}") String excelPath) {
        this.db = ExcelDatabaseManager.getInstance(excelPath);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Sheet sheet = db.getSheet(ExcelSheetConstants.USERS);
        if (sheet == null) return Optional.empty();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            User user = mapper.fromRow(row);
            if (email.equalsIgnoreCase(user.getEmail())) return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(String id) {
        Sheet sheet = db.getSheet(ExcelSheetConstants.USERS);
        if (sheet == null) return Optional.empty();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            User user = mapper.fromRow(row);
            if (id.equals(user.getId())) return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        Sheet sheet = db.getOrCreateSheet(ExcelSheetConstants.USERS);
        Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
        mapper.writeToRow(user, newRow);
        db.save();
        return user;
    }
}
