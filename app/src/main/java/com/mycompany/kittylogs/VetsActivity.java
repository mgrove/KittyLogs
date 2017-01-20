package com.mycompany.kittylogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class VetsActivity extends AppCompatActivity {

    public final static String CLICKED_VET = "com.example.kittylogs.VET_NAME";
    ListView listView;
    Button btnAdd;
    private Cursor aCursor;
    VetsCursorAdapter aCursorAdapter;
    DBHelper aHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vets);
        setActionBar();

        populateListView();
        loadDataWithCursor();
     //   startCreateButtonAndInput();
    }

    private void setActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Vets");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void populateListView(){
        listView = (ListView) findViewById(R.id.vet_list);
        registerForContextMenu(listView);
     //   listView.setOnItemClickListener(this);
    }

//    private void startCreateButtonAndInput(){
//        btnAdd = (Button) findViewById(R.id.add_vet_button);
//        startClickListener();
//    }
//
//    private void startClickListener(){
//        btnAdd.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View arg0){
//                String
//            }
//        });
//    }

    public void openAddVetDialog(View view){
        AlertDialog.Builder addDialogBuilder = new AlertDialog.Builder(this);
        addDialogBuilder.setTitle(R.string.new_vet_dialog_title);

        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.add_vet_dialog, null);
        addDialogBuilder.setView(dialogLayout);

        final Spinner isEmergency = (Spinner)dialogLayout.findViewById(R.id.is_emergency_spinner);
        ArrayAdapter<CharSequence> emergencyAdapter = ArrayAdapter.createFromResource(this,R.array.yes_no_array,android.R.layout.simple_spinner_item);
        emergencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        isEmergency.setAdapter(emergencyAdapter);

        setAddButtons(addDialogBuilder, isEmergency);
        AlertDialog addDialog = addDialogBuilder.create();
        addDialog.show();
    }

    private void setAddButtons(AlertDialog.Builder builder, final Spinner isEmergency){
        builder.setPositiveButton("Add vet", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Dialog addDialog = (Dialog) dialog;
                EditText name = (EditText)addDialog.findViewById(R.id.vet_name_input);
                EditText phone = (EditText)addDialog.findViewById(R.id.vet_phone_input);
                EditText address = (EditText)addDialog.findViewById(R.id.vet_address_input);
                EditText website = (EditText)addDialog.findViewById(R.id.vet_website_input);

                String nameValue = name.getText().toString();
                String phoneValue = phone.getText().toString();
                String addressValue = address.getText().toString();
                String websiteValue = website.getText().toString();
                String isEmergencyValue = isEmergency.getSelectedItem().toString();

                aHelper.addEntryToDB(makeVetContentValues(nameValue, phoneValue, addressValue, websiteValue, isEmergencyValue), KittyLogsContract.VetsTable.TABLE_NAME);
                Log.d("Vets Table", DatabaseUtils.dumpCursorToString(aHelper.getTableCursorFromDB(KittyLogsContract.VetsTable.TABLE_NAME)));
                loadDataWithCursor();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){}
        });
    }

    private ContentValues makeVetContentValues(String name, String phone, String address, String website, String isEmergencyString){
        ContentValues values = new ContentValues();
        values.put(KittyLogsContract.VetsTable.COLUMN_VET_NAME, name);
        values.put(KittyLogsContract.VetsTable.COLUMN_PHONE, phone);
        values.put(KittyLogsContract.VetsTable.COLUMN_ADDRESS, address);
        values.put(KittyLogsContract.VetsTable.COLUMN_WEBSITE, website);
        values.put(KittyLogsContract.VetsTable.COLUMN_EMERGENCY, Extras.convertStringToBoolInt(isEmergencyString));
        return values;
    }

    private void loadDataWithCursor(){
        aHelper = new DBHelper(getApplicationContext());
        aCursor = aHelper.getTableCursorFromDB(KittyLogsContract.VetsTable.TABLE_NAME);
        aCursorAdapter = new VetsCursorAdapter(this, aCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
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
