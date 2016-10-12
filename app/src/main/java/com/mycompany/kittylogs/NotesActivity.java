package com.mycompany.kittylogs;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Calendar;

import static java.lang.System.currentTimeMillis;

//import java.util.Date;

public class NotesActivity extends AppCompatActivity {
    DBHelper aHelper;
    long catID;
    public final static String NOTES_CAT_ID = "com.mycompany.kittylogs.JOURNAL_CAT_ID";
    private Cursor aCursor;
    private NotesCursorAdapter aCursorAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        aHelper = new DBHelper(getApplicationContext());
        catID = getCatID();
        setActionBar();
        setFloatingActionButton();
        listView = (ListView) findViewById(R.id.note_list);
        loadDataWithCursor();
    }

    private long getCatID(){
        Intent intent = getIntent();
        return intent.getLongExtra(CatProfileActivity.CAT_ID,0);
    }

    private void setActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Notes for " + aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setFloatingActionButton(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.hide();
    }

    public void openAddDialog(View view){
        AlertDialog.Builder addDialogBuilder = new AlertDialog.Builder(this);
        addDialogBuilder.setMessage("This is the message")
                .setTitle("This is the title");
        final EditText input = new EditText(this);
        //input.setId(TEXT_ID);
        addDialogBuilder.setView(input);
        setAddButtons(addDialogBuilder, input);
        AlertDialog addDialog = addDialogBuilder.create();
        addDialog.show();
    }

    private void setAddButtons(AlertDialog.Builder builder, final EditText input){
        builder.setPositiveButton("Add note", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String value = input.getText().toString();
         //       Log.d("Time", Long.toString(currentTimeMillis()));
                Log.d("Table name", KittyLogsContract.NotesTable.TABLE_NAME);
                aHelper.addEntryToDB(makeNoteContentValues(value), KittyLogsContract.NotesTable.TABLE_NAME);
                Log.d("Notes Table", DatabaseUtils.dumpCursorToString(aHelper.getTableCursorFromDB(KittyLogsContract.NotesTable.TABLE_NAME)));
                loadDataWithCursor();
                return;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
    }

    private ContentValues makeNoteContentValues(String entry){
        ContentValues values = new ContentValues();
        values.put(KittyLogsContract.NotesTable.COLUMN_ENTRY, entry);
        values.put(KittyLogsContract.NotesTable.COLUMN_CAT_IDFK, catID);
        values.put(KittyLogsContract.NotesTable.COLUMN_DATE, currentTimeMillis());
    //    Log.d("Time", Long.toString(currentTimeMillis()));
        return values;
    }

    private void loadDataWithCursor(){
        aCursor = aHelper.getTableCursorFromDB(KittyLogsContract.NotesTable.TABLE_NAME);
        aCursorAdapter = new NotesCursorAdapter(this, aCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(aCursorAdapter);
        aHelper.close();
    }
}
