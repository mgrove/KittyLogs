package com.mycompany.kittylogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

/**
 * Created by System User on 1/14/2017.
 */

public abstract class LaunchActivity extends AppCompatActivity {
    DBHelper aHelper;
    long catID;
    public final static String CAT_ID = "com.mycompany.kittylogs.CAT_ID";
    String catName;
    protected int activityLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLayout = getActivityLayout();
        setContentView(activityLayout);
        aHelper = new DBHelper(getApplicationContext());
        setCatNameAndID();
        setActionBar();
    }

    private void setCatNameAndID(){
        catID = getCatID();
        catName = aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID);
    }

    private void setActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(makeTitleString());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void startClassActivity(Class<?> activity){
        Intent intent = new Intent(this, activity);
        intent.putExtra(CAT_ID, catID);
        startActivity(intent);
    }

    protected abstract long getCatID();

    protected abstract String makeTitleString();

    protected abstract int getActivityLayout();

}
