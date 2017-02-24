package com.mycompany.kittylogs;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class DiagnosesActivity extends CatDataActivity {
    private Cursor aCursor;
    private DiagnosesCursorAdapter aCursorAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = (ListView) findViewById(R.id.diagnoses_list);
        loadDataWithCursor();

    }

    protected String getMainTableName(){
        return KittyLogsContract.DiagnosesTable.TABLE_NAME;
    }

    protected String getMainTableColumnCatIDFK(){
        return KittyLogsContract.DiagnosesTable.COLUMN_CAT_IDFK;
    }

    protected int getActivityLayout(){
        return R.layout.activity_diagnoses;
    }

    public String makeTitleString(){
        return "Diagnoses for " + aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID);
    }

    protected void loadDataWithCursor(){
        aCursor = aHelper.getTableCursorForCatFromDB(KittyLogsContract.DiagnosesTable.TABLE_NAME, KittyLogsContract.DiagnosesTable.COLUMN_CAT_IDFK, catID);
        aCursorAdapter = new DiagnosesCursorAdapter(this, aCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(aCursorAdapter);
        aHelper.close();
    }

    public class DiagnosesCursorAdapter extends CursorAdapter {
        private LayoutInflater cursorInflater;
        private final Context context;
        DBHelper aHelper = new DBHelper(getApplicationContext());

        protected DiagnosesCursorAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
            this.context = context;
            cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor){
            TextView dateTextView = (TextView) view.findViewById(R.id.date_name_row_date);
            TextView diagnosisTextView = (TextView) view.findViewById(R.id.date_name_row_name);
            Long visitID = cursor.getLong(cursor.getColumnIndex(KittyLogsContract.DiagnosesTable.COLUMN_VET_VISIT_IDFK));
            Long date = Long.parseLong(aHelper.getValueFromDB(KittyLogsContract.VetVisitsTable.COLUMN_DATE,
                    KittyLogsContract.VetVisitsTable.TABLE_NAME,
                    KittyLogsContract.VetVisitsTable._ID,
                    visitID));

            String dateString = Extras.convertMillisecondsToDate(date);
            String diagnosis = cursor.getString(cursor.getColumnIndex(KittyLogsContract.DiagnosesTable.COLUMN_DIAGNOSIS));
            dateTextView.setText(dateString);
            diagnosisTextView.setText(diagnosis);
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent){
            return cursorInflater.from(context).inflate(R.layout.date_name_row_view, parent, false);
        }
    }

}
