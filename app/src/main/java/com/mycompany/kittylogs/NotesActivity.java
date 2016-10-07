package com.mycompany.kittylogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class NotesActivity extends AppCompatActivity {
    DBHelper aHelper;
    long catID;
    public final static String NOTES_CAT_ID = "com.mycompany.kittylogs.JOURNAL_CAT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        aHelper = new DBHelper(getApplicationContext());
        catID = getCatID();
        setActionBar();
        setFloatingActionButton();
    }

    private long getCatID(){
        Intent intent = getIntent();
        return intent.getLongExtra(CatProfileActivity.CAT_ID,0);
    }

    private void setActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Notes for " + aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setFloatingActionButton(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.hide();
    }

}
