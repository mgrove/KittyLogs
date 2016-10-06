package com.mycompany.kittylogs;

import android.provider.BaseColumns;

/**
 * Created by System User on 9/22/2016.
 */
public final class KittyLogsContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";


    private KittyLogsContract(){}

    public static class CatsTable implements BaseColumns {
        public static final String TABLE_NAME = "cats";
        //        public static final String COLUMN_PK = "id";
        public static final String COLUMN_CAT_NAME = "name";
        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," + COLUMN_CAT_NAME + TEXT_TYPE + ")";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class JournalTable implements BaseColumns{
        public static final String TABLE_NAME = "journal";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_ENTRY = "entry";
        public static final String COLUMN_CAT_IDFK = "cat_idfk";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_DATE + " INTEGER," +
                COLUMN_ENTRY + " STRING," +
                "FOREIGN KEY (" + COLUMN_CAT_IDFK + ") REFERENCES " +
                CatsTable.TABLE_NAME + "(" + CatsTable._ID + "))";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}
