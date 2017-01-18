package com.mycompany.kittylogs;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class VetsActivity extends AppCompatActivity {

    public final static String CLICKED_VET = "com.example.kittylogs.VET_NAME";
    ListView listView;
    Button btnAdd;
    private Cursor aCursor;
    VetsCursorAdapter aCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vets);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public class VetsCursorAdapter extends CursorAdapter {
        private LayoutInflater cursorInflater;

        protected VetsCursorAdapter(Context context, Cursor cursor, int flags){
            super(context,cursor,flags);
            cursorInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor){}

        public View newView(Context context, Cursor cursor, ViewGroup parent){
            return cursorInflater.from(context).inflate(R.layout.cat_list_text_view, parent, false);
        }

    }

}
