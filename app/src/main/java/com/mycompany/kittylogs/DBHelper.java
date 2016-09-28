package com.mycompany.kittylogs;

import android.content.ContentValues;
import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;
import android.database.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

/**
 * Created by System User on 9/22/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "KittyLogs.db";

    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    private static String DROP_TABLE_STRING(String table){
        return "DROP TABLE IF EXISTS " + table;
    }

    public void addCatToDB(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KittyLogsContract.CatsTable.COLUMN_CAT_NAME,name);
        db.insert(KittyLogsContract.CatsTable.TABLE_NAME, null, contentValues);
        db.close();
    }

    public void removeCatFromDB(long id){
        Log.d("id", Long.toString(id));


        String selection = KittyLogsContract.CatsTable._ID;
        String[] selectionArgs = {Long.toString(id)};
        SQLiteDatabase db = this.getWritableDatabase();
//        String testQuery = "DELETE FROM cats WHERE _id = 29";
//        String deleteQuery = "DELETE FROM " + KittyLogsContract.CatsTable.TABLE_NAME + " WHERE " + selection + "=" + Long.toString(id);
//        Log.d("The delete query", testQuery);
        db.delete(KittyLogsContract.CatsTable.TABLE_NAME, "_id = ?", selectionArgs );
        Cursor cursor = db.rawQuery("SELECT * FROM cats", null);
        String cursorString = DatabaseUtils.dumpCursorToString(cursor);
        Log.d("The cursor",cursorString);
        //       String[] args = {"Aas"};
//        db.rawQuery(testQuery, null);
        db.close();
    }

    public List<String> getCatsFromDB(){
        List<String> cats = new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + KittyLogsContract.CatsTable.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                cats.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return cats;
    }

    public Cursor getCatsCursorFromDB(){
        String selectQuery = "SELECT * FROM " + KittyLogsContract.CatsTable.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(KittyLogsContract.CatsTable.CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DROP_TABLE_STRING(KittyLogsContract.CatsTable.TABLE_NAME));
        onCreate(db);
    }
}
