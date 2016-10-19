package com.mycompany.kittylogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class FoodActivity extends AppCompatActivity {
    DBHelper aHelper;
    long catID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        aHelper = new DBHelper(getApplicationContext());
        catID = getCatID();
        setActionBar();
        setFloatingActionButton();
    }

    private long getCatID(){
        Intent intent = getIntent();
        return intent.getLongExtra(CatProfileActivity.CAT_ID,0);
    }

    public void setActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Food for " + aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID));
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

    public void openAddFoodDialog(View view){
//        AlertDialog.Builder addDialogBuilder = new AlertDialog.Builder(this);
//        addDialogBuilder.setTitle(R.string.new_note_dialog_title);
//        final EditText input = new EditText(this);
//        addDialogBuilder.setView(input);
//        setAddButtons(addDialogBuilder, input);
//        AlertDialog addDialog = addDialogBuilder.create();
//        addDialog.show();
    }

//    private void setAddButtons(AlertDialog.Builder builder, final EditText input){
//        builder.setPositiveButton("Add note", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//    //            String value = input.getText().toString();
//                Log.d("Table name", KittyLogsContract.NotesTable.TABLE_NAME);
//   //             aHelper.addEntryToDB(makeNoteContentValues(value), KittyLogsContract.NotesTable.TABLE_NAME);
//                Log.d("Notes Table", DatabaseUtils.dumpCursorToString(aHelper.getTableCursorFromDB(KittyLogsContract.NotesTable.TABLE_NAME)));
//  //              loadDataWithCursor();
//                return;
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                return;
//            }
//        });
//    }

}
