package com.mycompany.kittylogs;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class VisitProfileActivity extends AppCompatActivity {
    DBHelper aHelper;
    long catID;
    long visitID;
    String catName;
    String vetName;
    TextView vetNameTextView;
    ListView vaccinesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_profile);
        setVariables();
        setActionBar();
        setVetTextView();
    }

    private void setVariables(){
        aHelper = new DBHelper(getApplicationContext());
        setCatAndVet();
        Log.d("Vet ID:", Long.toString(visitID));
        catName = aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID);
        Long vetID = Long.parseLong(aHelper.getValueFromDB(KittyLogsContract.VetVisitsTable.COLUMN_VET_IDFK, KittyLogsContract.VetVisitsTable.TABLE_NAME, KittyLogsContract.VetVisitsTable._ID, visitID));
        vetName = aHelper.getValueFromDB(KittyLogsContract.VetsTable.COLUMN_VET_NAME, KittyLogsContract.VetsTable.TABLE_NAME, KittyLogsContract.VetsTable._ID, vetID);
    }

    private void setVetTextView(){
            vetNameTextView = (TextView)findViewById(R.id.visit_profile_vet_name);
            vetNameTextView.setTextSize(40);
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
        setAddButtons(addDialogBuilder, input);
        AlertDialog addDialog = addDialogBuilder.create();
        addDialog.show();
    }

    private void setAddButtons(AlertDialog.Builder builder, final EditText input){
        builder.setPositiveButton("Add note", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String value = input.getText().toString();
                aHelper.addEntryToDB(makeVaccineContentValues(value), KittyLogsContract.VaccinesTable.TABLE_NAME);
                Log.d("Vaccines Table", DatabaseUtils.dumpCursorToString(aHelper.getTableCursorFromDB(KittyLogsContract.VaccinesTable.TABLE_NAME)));
        //        loadDataWithCursor();
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

}
