package com.mycompany.kittylogs;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class VisitProfileActivity extends AppCompatActivity {
    DBHelper aHelper;
    private Cursor vaccinesCursor;
    private VaccinesCursorAdapter vaccinesCursorAdapter;
    private Cursor diagnosesCursor;
    private DiagnosesCursorAdapter diagnosesCursorAdapter;
    private Cursor proceduresCursor;
    private ProceduresCursorAdapter proceduresCursorAdapter;

    long catID;
    long visitID;
    String catName;
    String vetName;
    String date;
    TextView vetNameTextView;
    TextView dateTextView;
    ListView vaccinesListView;
    ListView diagnosesListView;
    ListView proceduresListView;
    String selectedTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_profile);
        setVariables();
        setActionBar();
        setVetAndDateTextView();
        loadVaccineDataWithCursor();
        loadDiagnosisDataWithCursor();
        loadProcedureDataWithCursor();
    }

    private void setVariables(){
        vaccinesListView = (ListView) findViewById(R.id.visit_vaccine_list);
        diagnosesListView = (ListView) findViewById(R.id.visit_diagnosis_list);
        proceduresListView = (ListView) findViewById(R.id.visit_procedure_list);
        registerForContextMenu(vaccinesListView);
        registerForContextMenu(diagnosesListView);
        aHelper = new DBHelper(getApplicationContext());
        setCatAndVet();
        Log.d("Vet ID:", Long.toString(visitID));
        catName = aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID);
        Long vetID = Long.parseLong(aHelper.getValueFromDB(KittyLogsContract.VetVisitsTable.COLUMN_VET_IDFK, KittyLogsContract.VetVisitsTable.TABLE_NAME, KittyLogsContract.VetVisitsTable._ID, visitID));
        vetName = aHelper.getValueFromDB(KittyLogsContract.VetsTable.COLUMN_VET_NAME, KittyLogsContract.VetsTable.TABLE_NAME, KittyLogsContract.VetsTable._ID, vetID);
        String dateInMilliseconds = aHelper.getValueFromDB(KittyLogsContract.VetVisitsTable.COLUMN_DATE,
                KittyLogsContract.VetVisitsTable.TABLE_NAME,
                KittyLogsContract.VetVisitsTable._ID,
                visitID);
        date = Extras.convertMillisecondsToDate(Long.parseLong(dateInMilliseconds));
    }

    private void setVetAndDateTextView(){
        dateTextView = (TextView) findViewById(R.id.visit_profile_date);
        dateTextView.setTextSize(30);
        dateTextView.setText(date);

        vetNameTextView = (TextView)findViewById(R.id.visit_profile_vet_name);
        vetNameTextView.setTextSize(20);
        vetNameTextView.setText(vetName);

    }

    private void setCatAndVet() {
        Intent intent = getIntent();
        catID = intent.getLongExtra(VetVisitsActivity.CAT_ID, 0);
        visitID = intent.getLongExtra(VetVisitsActivity.VISIT_ID, 0);
    }

    private void setActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Visit profile for " + catName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void openAddVaccineDialog(View view){
        AlertDialog.Builder addDialogBuilder = new AlertDialog.Builder(this);
        addDialogBuilder.setTitle("New Vaccine");
        final EditText input = new EditText(this);
        addDialogBuilder.setView(input);
        setAddVaccineButtons(addDialogBuilder, input);
        AlertDialog addDialog = addDialogBuilder.create();
        addDialog.show();
    }

    private void setAddVaccineButtons(AlertDialog.Builder builder, final EditText input){
        builder.setPositiveButton("Add vaccine", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String value = input.getText().toString();
                aHelper.addEntryToDB(makeVaccineContentValues(value), KittyLogsContract.VaccinesTable.TABLE_NAME);
                Log.d("Vaccines Table", DatabaseUtils.dumpCursorToString(aHelper.getTableCursorFromDB(KittyLogsContract.VaccinesTable.TABLE_NAME)));
                loadVaccineDataWithCursor();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
    }

    private ContentValues makeVaccineContentValues(String entry){
        ContentValues values = new ContentValues();
        values.put(KittyLogsContract.VaccinesTable.COLUMN_VACCINE_NAME, entry);
        values.put(KittyLogsContract.VaccinesTable.COLUMN_CAT_IDFK, catID);
        values.put(KittyLogsContract.VaccinesTable.COLUMN_VET_VISIT_IDFK, visitID);
        return values;
    }

    protected void loadVaccineDataWithCursor(){
        vaccinesCursor = aHelper.getTableCursorForCatAndVisitFromDB(KittyLogsContract.VaccinesTable.TABLE_NAME, KittyLogsContract.VaccinesTable.COLUMN_CAT_IDFK, catID, KittyLogsContract.VaccinesTable.COLUMN_VET_VISIT_IDFK, visitID);
        vaccinesCursorAdapter = new VisitProfileActivity.VaccinesCursorAdapter(this, vaccinesCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        vaccinesListView.setAdapter(vaccinesCursorAdapter);
        aHelper.close();
    }

    public void openAddDiagnosisDialog(View view){
        AlertDialog.Builder addDialogBuilder = new AlertDialog.Builder(this);
        addDialogBuilder.setTitle("New Diagnosis");
        final EditText input = new EditText(this);
        addDialogBuilder.setView(input);
        setAddDiagnosisButtons(addDialogBuilder, input);
        AlertDialog addDialog = addDialogBuilder.create();
        addDialog.show();
    }

    private void setAddDiagnosisButtons(AlertDialog.Builder builder, final EditText input){
        builder.setPositiveButton("Add diagnosis", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String value = input.getText().toString();
                aHelper.addEntryToDB(makeDiagnosisContentValues(value), KittyLogsContract.DiagnosesTable.TABLE_NAME);
                Log.d("Diagnoses Table", DatabaseUtils.dumpCursorToString(aHelper.getTableCursorFromDB(KittyLogsContract.DiagnosesTable.TABLE_NAME)));
                loadDiagnosisDataWithCursor();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
    }

    private ContentValues makeDiagnosisContentValues(String entry){
        ContentValues values = new ContentValues();
        values.put(KittyLogsContract.DiagnosesTable.COLUMN_DIAGNOSIS, entry);
        values.put(KittyLogsContract.DiagnosesTable.COLUMN_CAT_IDFK, catID);
        values.put(KittyLogsContract.DiagnosesTable.COLUMN_VET_VISIT_IDFK, visitID);
        return values;
    }

    protected void loadDiagnosisDataWithCursor(){
        diagnosesCursor = aHelper.getTableCursorForCatAndVisitFromDB(KittyLogsContract.DiagnosesTable.TABLE_NAME, KittyLogsContract.DiagnosesTable.COLUMN_CAT_IDFK, catID, KittyLogsContract.DiagnosesTable.COLUMN_VET_VISIT_IDFK, visitID);
        diagnosesCursorAdapter = new VisitProfileActivity.DiagnosesCursorAdapter(this, diagnosesCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        diagnosesListView.setAdapter(diagnosesCursorAdapter);
        aHelper.close();
    }

    public void openAddProcedureDialog(View view){
        AlertDialog.Builder addDialogBuilder = new AlertDialog.Builder(this);
        addDialogBuilder.setTitle("New Procedure");
        final EditText input = new EditText(this);
        addDialogBuilder.setView(input);
        setAddProcedureButtons(addDialogBuilder, input);
        AlertDialog addDialog = addDialogBuilder.create();
        addDialog.show();
    }

    private void setAddProcedureButtons(AlertDialog.Builder builder, final EditText input){
        builder.setPositiveButton("Add procedure", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String value = input.getText().toString();
                aHelper.addEntryToDB(makeProcedureContentValues(value), KittyLogsContract.ProceduresTable.TABLE_NAME);
                Log.d("Diagnoses Table", DatabaseUtils.dumpCursorToString(aHelper.getTableCursorFromDB(KittyLogsContract.ProceduresTable.TABLE_NAME)));
                loadProcedureDataWithCursor();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
    }

    private ContentValues makeProcedureContentValues(String entry){
        ContentValues values = new ContentValues();
        values.put(KittyLogsContract.ProceduresTable.COLUMN_PROCEDURE, entry);
        values.put(KittyLogsContract.ProceduresTable.COLUMN_CAT_IDFK, catID);
        values.put(KittyLogsContract.ProceduresTable.COLUMN_VET_VISIT_IDFK, visitID);
        return values;
    }

    protected void loadProcedureDataWithCursor(){
        proceduresCursor = aHelper.getTableCursorForCatAndVisitFromDB(KittyLogsContract.ProceduresTable.TABLE_NAME, KittyLogsContract.ProceduresTable.COLUMN_CAT_IDFK, catID, KittyLogsContract.ProceduresTable.COLUMN_VET_VISIT_IDFK, visitID);
        proceduresCursorAdapter = new VisitProfileActivity.ProceduresCursorAdapter(this, proceduresCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        proceduresListView.setAdapter(proceduresCursorAdapter);
        aHelper.close();
    }



    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        switch(v.getId()){
            case R.id.visit_vaccine_list:
                selectedTable = KittyLogsContract.VaccinesTable.TABLE_NAME;
                break;
            case R.id.visit_diagnosis_list:
                selectedTable = KittyLogsContract.DiagnosesTable.TABLE_NAME;
                break;
        }
        inflater.inflate(R.menu.menu_delete, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.cnt_mnu_delete:
                makeDeleteDialog(info.id, aHelper);
                break;
        }
        return true;
    }

    protected void makeDeleteDialog(final long rowID, final DBHelper aHelper) {
        AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(this);
        makeDeleteMessage(deleteDialogBuilder);
        setDeleteButtons(deleteDialogBuilder, rowID, aHelper);
        AlertDialog deleteDialog = deleteDialogBuilder.create();
        deleteDialog.show();
    }

    private void makeDeleteMessage(AlertDialog.Builder deleteDialogBuilder){
        final String deleteMessageString = this.getString(R.string.delete_dialog_message) + " this?";
        deleteDialogBuilder.setMessage(deleteMessageString)
                .setTitle(R.string.delete_dialog_title);
    }

    private void setDeleteButtons(AlertDialog.Builder deleteDialogBuilder, final long rowID, final DBHelper aHelper){
        deleteDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteEntryWithMenu(rowID);
            }
        });
        deleteDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id){}
        });
    }

    protected void deleteEntryWithMenu(final long rowID){
        aHelper.removeEntryFromDB(rowID, selectedTable);
        loadVaccineDataWithCursor();
        loadDiagnosisDataWithCursor();
        loadProcedureDataWithCursor();
    }


    public class VaccinesCursorAdapter extends CursorAdapter {

        private LayoutInflater cursorInflater;
        private final Context context;

        protected VaccinesCursorAdapter(Context context, Cursor cursor, int flags){
            super(context, cursor, flags);
            this.context = context;
            cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor){
            TextView vaccineTextView = (TextView) view.findViewById(R.id.small_basic_row_view);
            String name = cursor.getString(cursor.getColumnIndex(KittyLogsContract.VaccinesTable.COLUMN_VACCINE_NAME));
            vaccineTextView.setText(name);
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent){
            return cursorInflater.from(context).inflate(R.layout.small_basic_row_view, parent, false);
        }

    }

    public class DiagnosesCursorAdapter extends CursorAdapter {

        private LayoutInflater cursorInflater;
        private final Context context;

        protected DiagnosesCursorAdapter(Context context, Cursor cursor, int flags){
            super(context, cursor, flags);
            this.context = context;
            cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor){
            TextView diagnosisTextView = (TextView) view.findViewById(R.id.small_basic_row_view);
            String name = cursor.getString(cursor.getColumnIndex(KittyLogsContract.DiagnosesTable.COLUMN_DIAGNOSIS));
            diagnosisTextView.setText(name);
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent){
            return cursorInflater.from(context).inflate(R.layout.small_basic_row_view, parent, false);
        }

    }

    public class ProceduresCursorAdapter extends CursorAdapter {

        private LayoutInflater cursorInflater;
        private final Context context;

        protected ProceduresCursorAdapter(Context context, Cursor cursor, int flags){
            super(context, cursor, flags);
            this.context = context;
            cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor){
            TextView procedureTextView = (TextView) view.findViewById(R.id.small_basic_row_view);
            String name = cursor.getString(cursor.getColumnIndex(KittyLogsContract.ProceduresTable.COLUMN_PROCEDURE));
            procedureTextView.setText(name);
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent){
            return cursorInflater.from(context).inflate(R.layout.small_basic_row_view, parent, false);
        }

    }

}
