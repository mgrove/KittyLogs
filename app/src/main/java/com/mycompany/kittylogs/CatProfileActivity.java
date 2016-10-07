package com.mycompany.kittylogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CatProfileActivity extends AppCompatActivity {
    TextView textView;
    DBHelper aHelper;
    long catID;
    String catName;
    public final static String CAT_ID = "com.mycompany.kittylogs.CAT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_profile);
        aHelper = new DBHelper(getApplicationContext());
        setCatNameAndID();
        makeNameView(catName);
        setActionBar();
        setFloatingActionButton();
    }

    private void setCatNameAndID(){
        catID = getCatID();
        catName = aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID);
    }

    private long getCatID(){
        Intent intent = getIntent();
        return intent.getLongExtra(HomeScreen.CLICKED_CAT, 0);
    }

    private void makeNameView(String catName){
        textView = (TextView)findViewById(R.id.cat_name);
        textView.setTextSize(40);
        textView.setText(catName);
    }

    private void setActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(catName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setFloatingActionButton(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.hide();
    }

    public void startNotesActivity(View view){
        Log.d("Cat ID from profile: ", Long.toString(catID));
        Intent intent = new Intent(this, NotesActivity.class);
        intent.putExtra(CAT_ID, catID);
        startActivity(intent);
    }

}
