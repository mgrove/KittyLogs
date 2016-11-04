package com.mycompany.kittylogs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public abstract class CatDataActivity extends AppCompatActivity {
    DBHelper aHelper;
    long catID;
    protected int activityLayout;
    protected String mainTableName;
    protected String mainTableColumnCatIDFK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVariables();
        setContentView(activityLayout);
        setActionBar();
    }

    protected void setVariables(){
        aHelper = new DBHelper(getApplicationContext());
        catID = getCatID();
        activityLayout = getActivityLayout();
        mainTableName = getMainTableName();
        Log.d("Main table name:",mainTableName);
        mainTableColumnCatIDFK = getMainTableColumnCatIDFK();
        Log.d("catID from setVariables", Long.toString(catID));
    }

    private long getCatID(){
        Intent intent = getIntent();
        return intent.getLongExtra(CatProfileActivity.CAT_ID,0);
    }

    private void setActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(makeTitleString());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected abstract void loadDataWithCursor();

    protected abstract String makeTitleString();

    protected abstract String getMainTableName();
    protected abstract String getMainTableColumnCatIDFK();
    protected abstract int getActivityLayout();
}
