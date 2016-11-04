package com.mycompany.kittylogs;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

public abstract class CatDataActivity extends AppCompatActivity {
    DBHelper aHelper;
    long catID;
    protected Cursor aCursor;
    protected CursorAdapter aCursorAdapter;
    protected View activityLayout;
    protected String mainTableName;
    protected String mainTableColumnCatIDFK;
    protected AdapterView dataDisplayLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVariables();
        setContentView(activityLayout);
        setActionBar();
        loadDataWithCursor();
    }

    protected void setVariables(){
        aHelper = new DBHelper(getApplicationContext()); // May cause problems
        catID = getCatID();
        activityLayout = getActivityLayout();
        mainTableName = getMainTableName();
        mainTableColumnCatIDFK = getMainTableColumnCatIDFK();
        dataDisplayLayout = getDataDisplayLayout();
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

    protected abstract String makeTitleString();

    protected void loadDataWithCursor(){
        aCursor = aHelper.getTableCursorForCatFromDB(mainTableName, mainTableColumnCatIDFK, catID);
        aCursorAdapter = new NotesCursorAdapter(this, aCursor, android.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        dataDisplayLayout.setAdapter(aCursorAdapter);
        aHelper.close();
    }

    protected abstract String getMainTableName();
    protected abstract String getMainTableColumnCatIDFK();
    protected abstract View getActivityLayout();
    protected abstract AdapterView getDataDisplayLayout();
}
