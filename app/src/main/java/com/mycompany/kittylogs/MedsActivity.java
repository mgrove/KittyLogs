package com.mycompany.kittylogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MedsActivity extends CatDataActivity {

    private Cursor aCursor;
    private MedsCursorAdapter aCursorAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = (ListView) findViewById(R.id.meds_list);
        registerForContextMenu(listView);
        loadDataWithCursor();
    }

    protected String getMainTableName(){
        return KittyLogsContract.MedsTable.TABLE_NAME;
    }

    protected String getMainTableColumnCatIDFK(){
        return KittyLogsContract.MedsTable.COLUMN_CAT_IDFK;
    }

    protected int getActivityLayout(){
        return R.layout.activity_meds;
    }

    protected String makeTitleString(){
        return "Meds for " + aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID);
    }

    public void openAddMedDialog(View view){
        AlertDialog.Builder addDialogBuilder = new AlertDialog.Builder(this);
        addDialogBuilder.setTitle("New medication");

        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.add_med_dialog, null);
        addDialogBuilder.setView(dialogLayout);
        setAddButtons(addDialogBuilder);
        AlertDialog addDialog = addDialogBuilder.create();
        addDialog.show();
    }

    private void setAddButtons(AlertDialog.Builder builder) {
        builder.setPositiveButton("Add food", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Dialog addDialog = (Dialog) dialog;
                EditText name = (EditText)addDialog.findViewById(R.id.med_name_input);
                EditText dosage = (EditText)addDialog.findViewById(R.id.dosage_input);
                EditText notes = (EditText)addDialog.findViewById(R.id.med_notes_input);

                String nameValue = name.getText().toString();
                String dosageValue = dosage.getText().toString();
                String notesValue = notes.getText().toString();
                aHelper.addEntryToDB(makeMedContentValues(nameValue, dosageValue, notesValue), KittyLogsContract.MedsTable.TABLE_NAME);
                loadDataWithCursor();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
    }

    private ContentValues makeMedContentValues(String med, String dosage, String notes){
        ContentValues values = new ContentValues();
        values.put(KittyLogsContract.MedsTable.COLUMN_MED_NAME, med);
        values.put(KittyLogsContract.MedsTable.COLUMN_DOSAGE, dosage);
        values.put(KittyLogsContract.MedsTable.COLUMN_NOTES, notes);
        values.put(KittyLogsContract.MedsTable.COLUMN_CAT_IDFK, catID);
        values.put(KittyLogsContract.MedsTable.COLUMN_IS_DONE, 0);
        return values;
    }

    protected void loadDataWithCursor(){
        aCursor = aHelper.getTableCursorForCatFromDB(KittyLogsContract.MedsTable.TABLE_NAME, KittyLogsContract.MedsTable.COLUMN_CAT_IDFK, catID);
        aCursorAdapter = new MedsCursorAdapter(this, aCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(aCursorAdapter);
        aHelper.close();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.superOnCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.med_actions, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.cnt_mnu_mark_done:
                aHelper.editEntryInDB(makeDoneContentValues(), info.id, KittyLogsContract.MedsTable.TABLE_NAME);
                break;
            case R.id.cnt_mnu_delete:
                super.makeDeleteDialog(info.id, aHelper);
                break;
        }
        return true;
    }

    private ContentValues makeDoneContentValues(){
        ContentValues values = new ContentValues();
        values.put(KittyLogsContract.MedsTable.COLUMN_IS_DONE, 1);
        return values;
    }

    public class MedsCursorAdapter extends android.support.v4.widget.CursorAdapter {
        private LayoutInflater cursorInflater;
        private final Context context;

        protected MedsCursorAdapter(Context context, Cursor cursor, int flags){
            super(context, cursor, flags);
            this.context = context;
            cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor){
            TextView medTextView = (TextView) view.findViewById(R.id.med_name);
            TextView dosageTextView = (TextView) view.findViewById(R.id.med_dosage);
            TextView notesTextView = (TextView) view.findViewById(R.id.med_notes);

            String med = cursor.getString(cursor.getColumnIndex(KittyLogsContract.MedsTable.COLUMN_MED_NAME));
            String dosage = cursor.getString(cursor.getColumnIndex(KittyLogsContract.MedsTable.COLUMN_DOSAGE));
            String notes = cursor.getString(cursor.getColumnIndex(KittyLogsContract.MedsTable.COLUMN_NOTES));

            medTextView.setText(med);
            dosageTextView.setText(dosage);
            notesTextView.setText(notes);
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return cursorInflater.from(context).inflate(R.layout.meds_row_view, parent, false);
        }

    }

}
