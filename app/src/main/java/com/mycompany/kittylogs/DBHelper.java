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

    public void addEntryToDB(ContentValues values, String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(tableName, null, values);
        db.close();
    }

    public void removeEntryFromDB(long id, String tableName){
        String[] selectionArgs = {Long.toString(id)};
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, "_id = ?", selectionArgs);
        db.close();
    }

    public void editEntryInDB(ContentValues values, long id, String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] selectionArgs = {Long.toString(id)};
        db.update(tableName, values,"_id = ?", selectionArgs);
        db.close();
    }

    public String getValueFromDB(String columnName, String tableName, String tableID, long id){
        String[] selectionArgs = {Long.toString(id)};
        SQLiteDatabase db = this.getReadableDatabase();
        String getCatQuery = "SELECT " + columnName +
                " FROM " + tableName +
                " WHERE " + tableID + "= ?";
        String result = DatabaseUtils.stringForQuery(db, getCatQuery, selectionArgs);
        db.close();
        return result;
    }

    public Cursor getTableCursorFromDB(String tableName){
        String selectQuery = "SELECT * FROM " + tableName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor query = db.rawQuery(selectQuery, null);
        return query;
    }

    public Cursor getTableCursorForCatFromDB(String tableName, String catIDName, long catID){
        String selectQuery = "Select * FROM " + tableName + " WHERE " + catIDName + "=" + catID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor query = db.rawQuery(selectQuery, null);
        return query;
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(KittyLogsContract.CatsTable.CREATE_TABLE);
        db.execSQL(KittyLogsContract.NotesTable.CREATE_TABLE);
        Log.d("Cats SQL", KittyLogsContract.CatsTable.CREATE_TABLE);
        Log.d("Notes SQL", KittyLogsContract.NotesTable.CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DROP_TABLE_STRING(KittyLogsContract.CatsTable.TABLE_NAME));
        db.execSQL(DROP_TABLE_STRING(KittyLogsContract.NotesTable.TABLE_NAME));
        onCreate(db);
    }
}
