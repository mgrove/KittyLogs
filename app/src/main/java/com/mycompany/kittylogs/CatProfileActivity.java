package com.mycompany.kittylogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CatProfileActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 //       getSupportActionBar().hide();
        setContentView(R.layout.activity_cat_profile);
        makeNameView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private void makeNameView(){
        Intent intent = getIntent();
        long catID = intent.getLongExtra(HomeScreen.CLICKED_CAT, 0);
        textView = (TextView)findViewById(R.id.cat_name);
        textView.setTextSize(40);
        DBHelper aHelper = new DBHelper(getApplicationContext());
        textView.setText(aHelper.getCatNameFromDB(catID));
    }

}
