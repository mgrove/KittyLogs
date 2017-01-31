package com.mycompany.kittylogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

public class VisitProfileActivity extends AppCompatActivity {
    DBHelper aHelper;
    long catID;
    long visitID;
    String catName;
    String vetName;
    TextView vetNameTextView;
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

}
