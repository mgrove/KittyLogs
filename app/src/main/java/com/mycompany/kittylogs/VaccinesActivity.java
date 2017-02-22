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
import android.widget.ListView;
import android.widget.TextView;

public class VaccinesActivity extends CatDataActivity {
    private Cursor aCursor;
    private VaccinesCursorAdapter aCursorAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = (ListView) findViewById(R.id.vaccines_list);
        registerForContextMenu(listView);
        loadDataWithCursor();

        //these will not be needed once CatDataActivity is extended
//        setContentView(R.layout.activity_vaccines);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected String getMainTableName(){
        return KittyLogsContract.VaccinesTable.TABLE_NAME;
    }

    protected String getMainTableColumnCatIDFK(){
        return KittyLogsContract.VaccinesTable.COLUMN_CAT_IDFK;
    }

    protected int getActivityLayout(){
        return R.layout.activity_vaccines;
    }

    public String makeTitleString(){
        return "Vaccines for " + aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID);
    }

    protected void loadDataWithCursor(){
        aCursor = aHelper.getTableCursorForCatFromDB(KittyLogsContract.VaccinesTable.TABLE_NAME, KittyLogsContract.VaccinesTable.COLUMN_CAT_IDFK, catID);
        aCursorAdapter = new VaccinesCursorAdapter(this, aCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(aCursorAdapter);
        aHelper.close();
    }

    public class VaccinesCursorAdapter extends CursorAdapter {
        private LayoutInflater cursorInflater;
        private final Context context;
        DBHelper aHelper = new DBHelper(getApplicationContext());

        protected VaccinesCursorAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
            this.context = context;
            cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor){
            TextView dateTextView = (TextView) view.findViewById(R.id.vaccine_date);
            TextView nameTextView = (TextView) view.findViewById(R.id.vaccine_name);
            Long visitID = cursor.getLong(cursor.getColumnIndex(KittyLogsContract.VaccinesTable.COLUMN_VET_VISIT_IDFK));
            Long date = Long.parseLong(aHelper.getValueFromDB(KittyLogsContract.VetVisitsTable.COLUMN_DATE,
                    KittyLogsContract.VetVisitsTable.TABLE_NAME,
                    KittyLogsContract.VetVisitsTable._ID,
                    visitID));

            String dateString = Extras.convertMillisecondsToDate(date);
            String name = cursor.getString(cursor.getColumnIndex(KittyLogsContract.VaccinesTable.COLUMN_VACCINE_NAME));
            dateTextView.setText(dateString);
            nameTextView.setText(name);
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent){
            return cursorInflater.from(context).inflate(R.layout.vaccine_row_view, parent, false);
        }
    }

}
