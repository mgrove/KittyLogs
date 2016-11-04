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
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.EditText;
import android.widget.ListView;

import static java.lang.System.currentTimeMillis;

public class NotesActivity extends CatDataActivity {
    public final static String NOTES_CAT_ID = "com.mycompany.kittylogs.JOURNAL_CAT_ID";
    private Cursor aCursor;
    private NotesCursorAdapter aCursorAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notes);
        listView = (ListView) findViewById(R.id.note_list);
        registerForContextMenu(listView);
        loadDataWithCursor();
    }

    protected String getMainTableName(){
        return KittyLogsContract.NotesTable.TABLE_NAME;
    }

    protected String getMainTableColumnCatIDFK(){
        return KittyLogsContract.NotesTable.COLUMN_CAT_IDFK;
    }

    protected int getActivityLayout(){
        return R.layout.activity_notes;
    }

    protected String makeTitleString(){
        return "Notes for " + aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID);
    }

    public void openAddNoteDialog(View view){
        AlertDialog.Builder addDialogBuilder = new AlertDialog.Builder(this);
        addDialogBuilder.setTitle(R.string.new_note_dialog_title);
        final EditText input = new EditText(this);
        addDialogBuilder.setView(input);
        setAddButtons(addDialogBuilder, input);
        AlertDialog addDialog = addDialogBuilder.create();
        addDialog.show();
    }

    private void setAddButtons(AlertDialog.Builder builder, final EditText input){
        builder.setPositiveButton("Add note", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String value = input.getText().toString();
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
        return values;
    }

    protected void loadDataWithCursor(){
        aCursor = aHelper.getTableCursorForCatFromDB(KittyLogsContract.NotesTable.TABLE_NAME, KittyLogsContract.NotesTable.COLUMN_CAT_IDFK, catID);
        aCursorAdapter = new NotesCursorAdapter(this, aCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(aCursorAdapter);
        aHelper.close();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_actions, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        DBHelper aHelper = new DBHelper(getApplicationContext());
        switch (item.getItemId()) {
            case R.id.cnt_mnu_delete:
                makeDeleteDialog(info.id, aHelper);
                break;
        }
        return true;
    }

    private void makeDeleteDialog(final long rowID, final DBHelper aHelper) {
        AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(this);
        makeDeleteMessage(deleteDialogBuilder);
        setDeleteButtons(deleteDialogBuilder, rowID, aHelper);
        AlertDialog deleteDialog = deleteDialogBuilder.create();
        deleteDialog.show();
    }

    private void makeDeleteMessage(AlertDialog.Builder deleteDialogBuilder){
        final String deleteMessageString = this.getString(R.string.delete_dialog_message) + " this note?";
        deleteDialogBuilder.setMessage(deleteMessageString)
                .setTitle(R.string.delete_dialog_title);
    }

    private void setDeleteButtons(AlertDialog.Builder deleteDialogBuilder, final long rowID, final DBHelper aHelper){
        deleteDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                aHelper.removeEntryFromDB(rowID, KittyLogsContract.NotesTable.TABLE_NAME);
                loadDataWithCursor();
                return;
            }
        });
        deleteDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
    }
}
