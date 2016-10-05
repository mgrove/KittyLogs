package com.mycompany.kittylogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CatProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 //       getSupportActionBar().hide();
        setContentView(R.layout.activity_cat_profile);
        Intent intent = getIntent();
        String message = intent.getStringExtra(HomeScreen.CLICKED_CAT);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText("Message");
        ViewGroup layout = (ViewGroup) findViewById(R.id.content_cat_profile);
        layout.addView(textView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
