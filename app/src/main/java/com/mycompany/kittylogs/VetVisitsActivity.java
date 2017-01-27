package com.mycompany.kittylogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import static java.lang.System.currentTimeMillis;

public class VetVisitsActivity extends CatDataActivity {
    private Cursor aCursor;
    private VetVisitsCursorAdapter aCursorAdapter;
    public final static String CAT_ID = "com.mycompany.kittylogs.CAT_ID";
    public final static String VISIT_ID = "com.mycompany.kittylogs.VISIT_ID";

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = (ListView) findViewById(R.id.vet_visit_list);
        registerForContextMenu(listView);
        loadDataWithCursor();
    }

    protected String getMainTableName(){
        return KittyLogsContract.VetVisitsTable.TABLE_NAME;
    }

    protected String getMainTableColumnCatIDFK(){
        return KittyLogsContract.VetVisitsTable.COLUMN_CAT_IDFK;
    }

    protected int getActivityLayout(){
        return R.layout.activity_vet_visits;
    }

    protected String makeTitleString(){
        return "Vet visits for " + aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID);
    }

    protected void loadDataWithCursor(){
        aCursor = aHelper.getTableCursorForCatFromDB(KittyLogsContract.VetVisitsTable.TABLE_NAME,KittyLogsContract.VetVisitsTable.COLUMN_CAT_IDFK, catID);
        aCursorAdapter = new VetVisitsCursorAdapter(this, aCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(aCursorAdapter);
        aHelper.close();
    }

    public void openAddVisitProfile(View view){
        Log.d("OpenAddVisitProfile", "lalala");
        AlertDialog.Builder addDialogBuilder = new AlertDialog.Builder(this);
        addDialogBuilder.setTitle("New visit");

        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.add_visit_dialog, null);
        addDialogBuilder.setView(dialogLayout);
        Cursor vetSpinnerCursor = aHelper.getTableCursorFromDB(KittyLogsContract.VetsTable.TABLE_NAME);

        final Spinner chooseVetListView = (Spinner)dialogLayout.findViewById(R.id.vet_spinner);
        NewVisitVetsCursorAdapter spinnerAdapter = new NewVisitVetsCursorAdapter(this, vetSpinnerCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
      //  spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        chooseVetListView.setAdapter(spinnerAdapter);

        setAddButtons(addDialogBuilder, chooseVetListView, this);
        AlertDialog addDialog = addDialogBuilder.create();
        addDialog.show();

//        Intent intent = new Intent(this, VisitProfileActivity.class);
//        intent.putExtra(CAT_ID, catID);
//        startActivity(intent);
    }

    private void setAddButtons(AlertDialog.Builder builder, final Spinner vetSpinner, final Context context){
        builder.setPositiveButton("Add visit", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Dialog addDialog = (Dialog) dialog;
                Long vetSpinnerID = ((Cursor)vetSpinner.getSelectedItem()).getLong(0);
                aHelper.addEntryToDB(makeVisitContentValues(vetSpinnerID), KittyLogsContract.VetVisitsTable.TABLE_NAME);
                Cursor visitCursor = aHelper.getLastAddedRecordFromDB(KittyLogsContract.VetVisitsTable.TABLE_NAME);
                Log.d("Column Index:", Integer.toString(visitCursor.getColumnIndex(KittyLogsContract.VetVisitsTable.COLUMN_VET_IDFK)));
                Long visitID = null;
                if (visitCursor != null && visitCursor.moveToFirst()) {
                    visitID = visitCursor.getLong(visitCursor.getColumnIndex(KittyLogsContract.VetVisitsTable.COLUMN_VET_IDFK));
                }
                Log.d("Visits Table", DatabaseUtils.dumpCursorToString(aHelper.getTableCursorFromDB(KittyLogsContract.VetVisitsTable.TABLE_NAME)));

                Log.d("Vet Selected:", Long.toString(vetSpinnerID));
                Intent intent = new Intent(context, VisitProfileActivity.class);
                intent.putExtra(CAT_ID, catID);
                intent.putExtra(VISIT_ID, visitID);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
    }

    public ContentValues makeVisitContentValues(long vetID){
        ContentValues values = new ContentValues();
        values.put(KittyLogsContract.VetVisitsTable.COLUMN_DATE, currentTimeMillis());
        values.put(KittyLogsContract.VetVisitsTable.COLUMN_VET_IDFK, vetID);
        values.put(KittyLogsContract.VetVisitsTable.COLUMN_CAT_IDFK, catID);
        values.put(KittyLogsContract.VetVisitsTable.COLUMN_DIRECTIONS, "None");
        return values;
    }

    public class VetVisitsCursorAdapter extends android.support.v4.widget.CursorAdapter {
        private LayoutInflater cursorInflater;
        private final Context context;
        private DBHelper aHelper = new DBHelper(getApplicationContext());

        protected VetVisitsCursorAdapter(Context context, Cursor cursor, int flags){
            super(context, cursor, flags);
            this.context = context;
            cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor){
            TextView dateTextView = (TextView) view.findViewById(R.id.visit_date);
            Log.d("Visited vet view:", Long.toString(R.id.visited_vet));
            TextView visitedVetTextView = (TextView) view.findViewById(R.id.visited_vet);
            String date = Extras.convertMillisecondsToDate(cursor.getLong(cursor.getColumnIndex(KittyLogsContract.VetVisitsTable.COLUMN_DATE)));
            Long visitedVetID = cursor.getLong(cursor.getColumnIndex(KittyLogsContract.VetVisitsTable.COLUMN_VET_IDFK));
            Log.d("Visited vet ID:", Long.toString(visitedVetID));

            String visitedVetString = aHelper.getValueFromDB(KittyLogsContract.VetsTable.COLUMN_VET_NAME, KittyLogsContract.VetsTable.TABLE_NAME, KittyLogsContract.VetsTable._ID, visitedVetID);
            dateTextView.setText(date);
            visitedVetTextView.setText(visitedVetString);
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return cursorInflater.from(context).inflate(R.layout.vet_visit_row_view, parent, false);
        }

    }

    public class NewVisitVetsCursorAdapter extends android.support.v4.widget.CursorAdapter{
        private LayoutInflater cursorInflater;

        protected NewVisitVetsCursorAdapter(Context context, Cursor cursor, int flags){
            super(context, cursor, flags);
            cursorInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor){
            TextView textView = (TextView) view.findViewById(R.id.add_visit_spinner_row);
            String vet_name = cursor.getString(cursor.getColumnIndex(KittyLogsContract.VetsTable.COLUMN_VET_NAME));
            textView.setText(vet_name);
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent){
            return cursorInflater.from(context).inflate(R.layout.add_visit_vet_row_view, parent, false);
        }
    }

}
