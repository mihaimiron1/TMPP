package com.library.infrastructure.excel.mapper;

import com.library.domain.enums.ItemType;
import com.library.domain.enums.LoanStatus;
import com.library.domain.model.Loan;
import com.library.infrastructure.excel.CellUtils;
import com.library.infrastructure.excel.RowMapper;
import org.apache.poi.ss.usermodel.Row;

// PATTERN: Adapter (concrete) — adapts Apache POI Row to/from Loan domain object
// borrowDate (col 4) and dueDate (col 5) are stored as Excel Date cells (not strings)
public class LoanRowMapper implements RowMapper<Loan> {

    @Override
    public Loan fromRow(Row row) {
        Loan loan = new Loan();
        loan.setId(CellUtils.getString(row, 0));
        loan.setUserId(CellUtils.getString(row, 1));
        loan.setItemId(CellUtils.getString(row, 2));
        loan.setItemType(parseItemType(CellUtils.getString(row, 3)));
        loan.setBorrowDate(CellUtils.getDateCell(row, 4));
        loan.setDueDate(CellUtils.getDateCell(row, 5));
        loan.setReturnDate(CellUtils.getDateCell(row, 6));   // null if empty
        loan.setStatus(parseLoanStatus(CellUtils.getString(row, 7)));
        loan.setPenaltyMDL(CellUtils.getDouble(row, 8));
        return loan;
    }

    @Override
    public void writeToRow(Loan loan, Row row) {
        CellUtils.setString  (row, 0, loan.getId());
        CellUtils.setString  (row, 1, loan.getUserId());
        CellUtils.setString  (row, 2, loan.getItemId());
        CellUtils.setString  (row, 3, loan.getItemType().name());
        CellUtils.setDateCell(row, 4, loan.getBorrowDate());   // Excel Date cell
        CellUtils.setDateCell(row, 5, loan.getDueDate());      // Excel Date cell
        CellUtils.setDateCell(row, 6, loan.getReturnDate());   // Excel Date cell (null → blank)
        CellUtils.setString  (row, 7, loan.getStatus().name());
        CellUtils.setDouble  (row, 8, loan.getPenaltyMDL());
    }

    private ItemType parseItemType(String s) {
        try { return ItemType.valueOf(s); }
        catch (IllegalArgumentException e) { return ItemType.BOOK; }
    }

    private LoanStatus parseLoanStatus(String s) {
        try { return LoanStatus.valueOf(s); }
        catch (IllegalArgumentException e) { return LoanStatus.ACTIVE; }
    }
}
