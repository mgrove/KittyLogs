package com.mycompany.kittylogs;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class WeightActivity extends AppCompatActivity {
    DBHelper aHelper;
    long catID;
    private Cursor aCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        aHelper = new DBHelper(getApplicationContext());
        catID = getCatID();
        setActionBar();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private long getCatID(){
        Intent intent = getIntent();
        return intent.getLongExtra(CatProfileActivity.CAT_ID,0);
    }

    public void setActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Weight for " + aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



}
