package com.library.infrastructure.excel;

public final class ExcelSheetConstants {

    // Sheet names
    public static final String USERS         = "Users";
    public static final String BOOKS         = "Books";
    public static final String MAGAZINES     = "Magazines";
    public static final String DVDS          = "DVDs";
    public static final String LOANS         = "Loans";
    public static final String NOTIFICATIONS = "Notifications";

    // Users columns
    public static final int USER_COL_ID            = 0;
    public static final int USER_COL_EMAIL          = 1;
    public static final int USER_COL_PASSWORD       = 2;
    public static final int USER_COL_NAME           = 3;
    public static final int USER_COL_REGISTERED_AT  = 4;

    // Books columns
    public static final int BOOK_COL_ID        = 0;
    public static final int BOOK_COL_TITLE     = 1;
    public static final int BOOK_COL_AUTHOR    = 2;
    public static final int BOOK_COL_ISBN      = 3;
    public static final int BOOK_COL_GENRE     = 4;
    public static final int BOOK_COL_YEAR      = 5;
    public static final int BOOK_COL_QUANTITY  = 6;
    public static final int BOOK_COL_AVAILABLE = 7;
    public static final int BOOK_COL_PENALTY   = 8;

    // Magazines columns
    public static final int MAG_COL_ID           = 0;
    public static final int MAG_COL_TITLE        = 1;
    public static final int MAG_COL_PUBLISHER    = 2;
    public static final int MAG_COL_ISSUE_NUMBER = 3;
    public static final int MAG_COL_MONTH        = 4;
    public static final int MAG_COL_GENRE        = 5;
    public static final int MAG_COL_YEAR         = 6;
    public static final int MAG_COL_QUANTITY     = 7;
    public static final int MAG_COL_AVAILABLE    = 8;
    public static final int MAG_COL_PENALTY      = 9;

    // DVDs columns
    public static final int DVD_COL_ID        = 0;
    public static final int DVD_COL_TITLE     = 1;
    public static final int DVD_COL_DIRECTOR  = 2;
    public static final int DVD_COL_GENRE     = 3;
    public static final int DVD_COL_DURATION  = 4;
    public static final int DVD_COL_YEAR      = 5;
    public static final int DVD_COL_QUANTITY  = 6;
    public static final int DVD_COL_AVAILABLE = 7;
    public static final int DVD_COL_PENALTY   = 8;

    // Loans columns
    public static final int LOAN_COL_ID          = 0;
    public static final int LOAN_COL_USER_ID     = 1;
    public static final int LOAN_COL_ITEM_ID     = 2;
    public static final int LOAN_COL_ITEM_TYPE   = 3;
    public static final int LOAN_COL_BORROW_DATE = 4;
    public static final int LOAN_COL_DUE_DATE    = 5;
    public static final int LOAN_COL_RETURN_DATE = 6;
    public static final int LOAN_COL_STATUS      = 7;
    public static final int LOAN_COL_PENALTY     = 8;

    // Notifications columns
    public static final int NOTIF_COL_ID         = 0;
    public static final int NOTIF_COL_USER_ID    = 1;
    public static final int NOTIF_COL_MESSAGE    = 2;
    public static final int NOTIF_COL_TYPE       = 3;
    public static final int NOTIF_COL_CREATED_AT = 4;
    public static final int NOTIF_COL_IS_READ    = 5;

    private ExcelSheetConstants() {}
}
