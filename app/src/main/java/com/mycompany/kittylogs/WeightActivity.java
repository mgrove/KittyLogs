package com.mycompany.kittylogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import static java.lang.System.currentTimeMillis;

public class WeightActivity extends AppCompatActivity {
    DBHelper aHelper;
    long catID;
    private Cursor aCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        aHelper = new DBHelper(getApplicationContext());
        catID = getCatID();
        setActionBar();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private long getCatID() {
        Intent intent = getIntent();
        return intent.getLongExtra(CatProfileActivity.CAT_ID, 0);
    }

    public void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Weight for " + aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void openAddWeightDialog(View view) {
        AlertDialog.Builder addDialogBuilder = new AlertDialog.Builder(this);
        addDialogBuilder.setTitle(R.string.new_note_dialog_title);
        LayoutInflater inflater = getLayoutInflater();
        addDialogBuilder.setView(inflater.inflate(R.layout.add_weight_dialog, null));
        setAddButtons(addDialogBuilder);
        AlertDialog addDialog = addDialogBuilder.create();
        addDialog.show();
    }

    private void setAddButtons(AlertDialog.Builder builder) {
        builder.setPositiveButton("Add note", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Dialog addDialog = (Dialog) dialog;
                EditText input = (EditText)addDialog.findViewById(R.id.weight_input_lalalalala);
                String value = input.getText().toString();
                Log.d("Table name", KittyLogsContract.NotesTable.TABLE_NAME);
                aHelper.addEntryToDB(makeWeightContentValues(value), KittyLogsContract.WeightTable.TABLE_NAME);
                Log.d("Weight Table", DatabaseUtils.dumpCursorToString(aHelper.getTableCursorFromDB(KittyLogsContract.WeightTable.TABLE_NAME)));
                //               loadDataWithCursor();
                return;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
    }

    private ContentValues makeWeightContentValues(String entry) {
        ContentValues values = new ContentValues();
        values.put(KittyLogsContract.WeightTable.COLUMN_WEIGHT, entry);
        values.put(KittyLogsContract.WeightTable.COLUMN_CAT_IDFK, catID);
        values.put(KittyLogsContract.WeightTable.COLUMN_DATE, currentTimeMillis());
        return values;
    }
}
