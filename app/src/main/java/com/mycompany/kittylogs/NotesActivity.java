package com.mycompany.kittylogs;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.app.AlertDialog;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import static java.lang.System.currentTimeMillis;

public class NotesActivity extends CatDataActivity {
 //   public final static String NOTES_CAT_ID = "com.mycompany.kittylogs.JOURNAL_CAT_ID";
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
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
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

    public class NotesCursorAdapter extends CursorAdapter {

        private LayoutInflater cursorInflater;
        private final Context context;


        protected NotesCursorAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
            this.context = context;
            cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor) {
            TextView noteTextView = (TextView) view.findViewById(R.id.note_text);
            TextView dateTextView = (TextView) view.findViewById(R.id.note_date);
            String notes = cursor.getString(cursor.getColumnIndex(KittyLogsContract.NotesTable.COLUMN_ENTRY));
            String dates = Extras.convertMillisecondsToDate(cursor.getLong(cursor.getColumnIndex(KittyLogsContract.NotesTable.COLUMN_DATE)));
            noteTextView.setText(notes);
            dateTextView.setText(dates);
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return cursorInflater.from(context).inflate(R.layout.note_row_view, parent, false);
        }
    }
}
