package com.mycompany.kittylogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MedicalRecordsActivity extends LaunchActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected long getCatID(){
        Intent intent = getIntent();
        return intent.getLongExtra(CatProfileActivity.CAT_ID, 0);
    }

    protected String makeTitleString(){
        return "Medical Records for " + catName;
    }

    protected int getActivityLayout(){
        return R.layout.activity_medical_records;
    }

    public void startMedsActivity(View view){
        startClassActivity(MedsActivity.class);
    }

    public void startVaccinesActivity(View view){
        startClassActivity(VaccinesActivity.class);
    }

    public void startDiagnosesActivity(View view){
        startClassActivity(DiagnosesActivity.class);
    }

    public void startVetsActivity(View view){
        startClassActivity(VetsActivity.class);
    }

    public void startVetVisitsActivity(View view){
        startClassActivity(VetVisitsActivity.class);
    }

}
