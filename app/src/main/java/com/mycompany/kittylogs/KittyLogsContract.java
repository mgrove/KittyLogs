package com.mycompany.kittylogs;

import android.provider.BaseColumns;

/**
 * Created by System User on 9/22/2016.
 */
public final class KittyLogsContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private KittyLogsContract(){}

    static class CatsTable implements BaseColumns {
        static final String TABLE_NAME = "cats";
        //        public static final String COLUMN_PK = "id";
        static final String COLUMN_CAT_NAME = "name";
        static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," + COLUMN_CAT_NAME + TEXT_TYPE + ")";
        protected static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    static class NotesTable implements BaseColumns{
        static final String TABLE_NAME = "notes";
        static final String COLUMN_DATE = "date";
        static final String COLUMN_ENTRY = "entry";
        static final String COLUMN_CAT_IDFK = "cat_idfk";

        static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_DATE + " INTEGER," +
                COLUMN_ENTRY + " TEXT," +
                COLUMN_CAT_IDFK + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_CAT_IDFK + ") REFERENCES " +
                CatsTable.TABLE_NAME + "(" + CatsTable._ID + "))";
        static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    static class FoodTable implements BaseColumns{
        static final String TABLE_NAME = "food";
        static final String COLUMN_DATE = "date";
        static final String COLUMN_BRAND = "brand";
        static final String COLUMN_FLAVOR = "flavor";
        static final String COLUMN_TYPE = "type";
        static final String COLUMN_IS_LIKED = "is_liked";
        static final String COLUMN_CAT_IDFK = "cat_idfk";

        static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
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

    static class WeightTable implements BaseColumns{
        static final String TABLE_NAME = "weights";
        static final String COLUMN_DATE = "date";
        static final String COLUMN_WEIGHT = "weight";
        static final String COLUMN_CAT_IDFK = "cat_idfk";

        static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_DATE + " INTEGER," +
                COLUMN_WEIGHT + " REAL," +
                COLUMN_CAT_IDFK + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_CAT_IDFK + ") REFERENCES " +
                CatsTable.TABLE_NAME + "(" + CatsTable._ID + "))";
    }

    static class VetsTable implements BaseColumns{
        static final String TABLE_NAME = "vets";
        static final String COLUMN_VET_NAME = "name";
        static final String COLUMN_PHONE = "phone";
        static final String COLUMN_ADDRESS = "address";
        static final String COLUMN_WEBSITE = "website";
        static final String COLUMN_EMERGENCY = "is_emergency_vet";

        static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_VET_NAME + " TEXT," +
                COLUMN_PHONE + " TEXT," +
                COLUMN_ADDRESS + " TEXT," +
                COLUMN_WEBSITE + " TEXT," +
                COLUMN_EMERGENCY + " INTEGER" + ")";
    }
}
