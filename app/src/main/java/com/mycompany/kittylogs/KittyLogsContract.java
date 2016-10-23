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

    public static class NotesTable implements BaseColumns{
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_ENTRY = "entry";
        public static final String COLUMN_CAT_IDFK = "cat_idfk";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_DATE + " INTEGER," +
                COLUMN_ENTRY + " TEXT," +
                COLUMN_CAT_IDFK + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_CAT_IDFK + ") REFERENCES " +
                CatsTable.TABLE_NAME + "(" + CatsTable._ID + "))";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    public static class FoodTable implements BaseColumns{
        public static final String TABLE_NAME = "food";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_BRAND = "brand";
        public static final String COLUMN_FLAVOR = "flavor";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_IS_LIKED = "is_liked";
        public static final String COLUMN_CAT_IDFK = "cat_idfk";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_DATE + " INTEGER," +
                COLUMN_BRAND + " TEXT," +
                COLUMN_FLAVOR + " TEXT," +
                COLUMN_TYPE + " TEXT," +
                COLUMN_IS_LIKED + " TEXT," +
                COLUMN_CAT_IDFK + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_CAT_IDFK + ") REFERENCES " +
                CatsTable.TABLE_NAME + "(" + CatsTable._ID + "))";
    }

    public static class WeightTable implements BaseColumns{
        public static final String TABLE_NAME = "weights";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_CAT_IDFK = "cat_idfk";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_DATE + " INTEGER," +
                COLUMN_WEIGHT + " REAL," +
                COLUMN_CAT_IDFK + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_CAT_IDFK + ") REFERENCES " +
                CatsTable.TABLE_NAME + "(" + CatsTable._ID + "))";
    }
}
