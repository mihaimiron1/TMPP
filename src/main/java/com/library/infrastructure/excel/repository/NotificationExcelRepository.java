package com.library.infrastructure.excel.repository;

import com.library.domain.model.Notification;
import com.library.domain.repository.NotificationRepository;
import com.library.infrastructure.excel.CellUtils;
import com.library.infrastructure.excel.ExcelDatabaseManager;
import com.library.infrastructure.excel.ExcelSheetConstants;
import com.library.infrastructure.excel.mapper.NotificationRowMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class NotificationExcelRepository implements NotificationRepository {

    private final ExcelDatabaseManager db;
    private final NotificationRowMapper mapper = new NotificationRowMapper();

    public NotificationExcelRepository(@Value("${library.excel.path}") String excelPath) {
        this.db = ExcelDatabaseManager.getInstance(excelPath);
    }

    @Override
    public Notification save(Notification notification) {
        Sheet sheet = db.getOrCreateSheet(ExcelSheetConstants.NOTIFICATIONS);
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        mapper.writeToRow(notification, row);
        db.save();
        return notification;
    }

    @Override
    public List<Notification> findByUserId(String userId) {
        List<Notification> result = new ArrayList<>();
        Sheet sheet = db.getSheet(ExcelSheetConstants.NOTIFICATIONS);
        if (sheet == null) return result;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            Notification n = mapper.fromRow(row);
            if (userId.equals(n.getUserId())) result.add(n);
        }
        return result;
    }

    @Override
    public void markAsRead(String id) {
        Sheet sheet = db.getSheet(ExcelSheetConstants.NOTIFICATIONS);
        if (sheet == null) return;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            if (id.equals(CellUtils.getString(row, ExcelSheetConstants.NOTIF_COL_ID))) {
                CellUtils.setBoolean(row, ExcelSheetConstants.NOTIF_COL_IS_READ, true);
                db.save();
                return;
            }
        }
    }
}
