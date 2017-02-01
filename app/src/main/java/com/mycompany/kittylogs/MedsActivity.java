package com.mycompany.kittylogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

public class MedsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meds);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected String getMainTableName(){
        return KittyLogsContract.MedsTable.TABLE_NAME;
    }

    protected String getMainTableColumnCatIDFK(){
        return KittyLogsContract.MedsTable.COLUMN_CAT_IDFK;
    }

    protected int getActivityLayout(){
        return R.layout.activity_meds;
    }

    public void openAddMedDialog(View view){
        AlertDialog.Builder addDialogBuilder = new AlertDialog.Builder(this);
        addDialogBuilder.setTitle("New medication");

        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.add_med_dialog, null);
        addDialogBuilder.setView(dialogLayout);
        setAddButtons(addDialogBuilder);
        AlertDialog addDialog = addDialogBuilder.create();
        addDialog.show();
    }

    private void setAddButtons(AlertDialog.Builder builder) {
        builder.setPositiveButton("Add food", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
    }

//    protected String makeTitleString(){
//        return "Meds for " +
//    }

    public class MedsCursorAdapter extends android.support.v4.widget.CursorAdapter {
        private LayoutInflater cursorInflater;
        private final Context context;

        protected MedsCursorAdapter(Context context, Cursor cursor, int flags){
            super(context, cursor, flags);
            this.context = context;
            cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor){

        }

        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return cursorInflater.from(context).inflate(R.layout.food_row_view, parent, false);
        }

    }

}
