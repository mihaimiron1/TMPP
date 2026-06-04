package com.library.infrastructure.excel.repository;

import com.library.domain.enums.LoanStatus;
import com.library.domain.model.Loan;
import com.library.domain.repository.LoanRepository;
import com.library.infrastructure.excel.CellUtils;
import com.library.infrastructure.excel.ExcelDatabaseManager;
import com.library.infrastructure.excel.ExcelSheetConstants;
import com.library.infrastructure.excel.mapper.LoanRowMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class LoanExcelRepository implements LoanRepository {

    private final ExcelDatabaseManager db;
    private final LoanRowMapper mapper = new LoanRowMapper();

    public LoanExcelRepository(@Value("${library.excel.path}") String excelPath) {
        this.db = ExcelDatabaseManager.getInstance(excelPath);
    }

    @Override
    public Loan save(Loan loan) {
        Sheet sheet = db.getOrCreateSheet(ExcelSheetConstants.LOANS);
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        mapper.writeToRow(loan, row);
        db.save();
        return loan;
    }

    @Override
    public Optional<Loan> findById(String id) {
        Sheet sheet = db.getSheet(ExcelSheetConstants.LOANS);
        if (sheet == null) return Optional.empty();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            Loan loan = mapper.fromRow(row);
            if (id.equals(loan.getId())) return Optional.of(loan);
        }
        return Optional.empty();
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        List<Loan> result = new ArrayList<>();
        Sheet sheet = db.getSheet(ExcelSheetConstants.LOANS);
        if (sheet == null) return result;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            Loan loan = mapper.fromRow(row);
            if (userId.equals(loan.getUserId())) result.add(loan);
        }
        return result;
    }

    @Override
    public List<Loan> findActiveByItemId(String itemId) {
        List<Loan> result = new ArrayList<>();
        Sheet sheet = db.getSheet(ExcelSheetConstants.LOANS);
        if (sheet == null) return result;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            Loan loan = mapper.fromRow(row);
            if (itemId.equals(loan.getItemId()) &&
                (loan.getStatus() == LoanStatus.ACTIVE || loan.getStatus() == LoanStatus.OVERDUE || loan.getStatus() == LoanStatus.EXTENDED)) {
                result.add(loan);
            }
        }
        return result;
    }

    @Override
    public List<Loan> findAllActive() {
        List<Loan> result = new ArrayList<>();
        Sheet sheet = db.getSheet(ExcelSheetConstants.LOANS);
        if (sheet == null) return result;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            Loan loan = mapper.fromRow(row);
            if (loan.getStatus() == LoanStatus.ACTIVE || loan.getStatus() == LoanStatus.OVERDUE || loan.getStatus() == LoanStatus.EXTENDED) {
                result.add(loan);
            }
        }
        return result;
    }

    @Override
    public void update(Loan loan) {
        Sheet sheet = db.getSheet(ExcelSheetConstants.LOANS);
        if (sheet == null) return;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            if (loan.getId().equals(CellUtils.getString(row, ExcelSheetConstants.LOAN_COL_ID))) {
                mapper.writeToRow(loan, row);
                db.save();
                return;
            }
        }
    }
}
