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
import android.widget.TextView;

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
        populateListView();
        loadDataWithCursor();
    }

    private void populateListView(){
        listView = (ListView) findViewById(R.id.vet_list);
     //   registerForContextMenu(listView);
     //   listView.setOnItemClickListener(this);
    }

    private void loadDataWithCursor(){
        DBHelper aHelper = new DBHelper(getApplicationContext());
        aCursor = aHelper.getTableCursorFromDB(KittyLogsContract.VetsTable.TABLE_NAME);
        listView.setAdapter(aCursorAdapter);
        aHelper.close();
    }

    public class VetsCursorAdapter extends CursorAdapter {
        private LayoutInflater cursorInflater;

        protected VetsCursorAdapter(Context context, Cursor cursor, int flags){
            super(context,cursor,flags);
            cursorInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public String convertBoolIntToString(int input){
            if (input==1){
                return "Yes";
            } else {
                return "No";
            }
        }

        public void bindView(View view, Context context, Cursor cursor){
            TextView nameTextView = (TextView) view.findViewById(R.id.vet_name);
            TextView phoneTextView = (TextView) view.findViewById(R.id.vet_phone);
            TextView addressTextView = (TextView) view.findViewById(R.id.vet_address);
            TextView websiteTextView = (TextView) view.findViewById(R.id.vet_website);
            TextView emergencyTextView = (TextView) view.findViewById(R.id.is_emergency_vet);
            String name = cursor.getString(cursor.getColumnIndex(KittyLogsContract.VetsTable.COLUMN_VET_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(KittyLogsContract.VetsTable.COLUMN_PHONE));
            String address = cursor.getString(cursor.getColumnIndex(KittyLogsContract.VetsTable.COLUMN_ADDRESS));
            String website = cursor.getString(cursor.getColumnIndex(KittyLogsContract.VetsTable.COLUMN_WEBSITE));
            String emergency = "Emergency vet? " + convertBoolIntToString(cursor.getInt(cursor.getColumnIndex(KittyLogsContract.VetsTable.COLUMN_EMERGENCY)));
            nameTextView.setText(name);
            phoneTextView.setText(phone);
            addressTextView.setText(address);
            websiteTextView.setText(website);
            emergencyTextView.setText(emergency);
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent){
            return cursorInflater.from(context).inflate(R.layout.vet_list_text_view, parent, false);
        }

    }

}
