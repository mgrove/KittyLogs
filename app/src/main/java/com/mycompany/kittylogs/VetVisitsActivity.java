package com.mycompany.kittylogs;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

public class VetVisitsActivity extends CatDataActivity {
    private Cursor aCursor;
    private VetVisitsCursorAdapter aCursorAdapter;
    public final static String CAT_ID = "com.mycompany.kittylogs.CAT_ID";

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
        Intent intent = new Intent(this, VisitProfileActivity.class);
        intent.putExtra(CAT_ID, catID);
        startActivity(intent);
    }

    public class VetVisitsCursorAdapter extends android.support.v4.widget.CursorAdapter {
        private LayoutInflater cursorInflater;
        private final Context context;
        private DBHelper aHelper;

        protected VetVisitsCursorAdapter(Context context, Cursor cursor, int flags){
            super(context, cursor, flags);
            this.context = context;
            cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor){
            TextView dateTextView = (TextView) view.findViewById(R.id.visit_date);
            TextView visitedVetTextView = (TextView) view.findViewById(R.id.visited_vet);
            String date = Extras.convertMillisecondsToDate(cursor.getLong(cursor.getColumnIndex(KittyLogsContract.VetVisitsTable.COLUMN_DATE)));
            Long visitedVetID = cursor.getLong(cursor.getColumnIndex(KittyLogsContract.VetVisitsTable.COLUMN_VET_IDFK));
            String visitedVetString = aHelper.getValueFromDB(KittyLogsContract.VetsTable.COLUMN_VET_NAME, KittyLogsContract.VetsTable.TABLE_NAME, KittyLogsContract.VetsTable._ID, visitedVetID);
            dateTextView.setText(date);
            visitedVetTextView.setText(visitedVetString);
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return cursorInflater.from(context).inflate(R.layout.food_row_view, parent, false);
        }

    }

}
