package com.mycompany.kittylogs;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by System User on 10/23/2016.
 */

public class WeightCursorAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;
    private final Context context;

    public WeightCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        this.context = context;
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView weightTextView = (TextView) view.findViewById(R.id.weight_weighttext);
        TextView dateTextView = (TextView) view.findViewById(R.id.weight_date);
        String weights = cursor.getString(cursor.getColumnIndex(KittyLogsContract.WeightTable.COLUMN_WEIGHT));
        String dates = Extras.convertMillisecondsToDate(cursor.getLong(cursor.getColumnIndex(KittyLogsContract.NotesTable.COLUMN_DATE)));
        weightTextView.setText(weights);
        dateTextView.setText(dates);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.from(context).inflate(R.layout.weight_row_view, parent, false);
    }

}
