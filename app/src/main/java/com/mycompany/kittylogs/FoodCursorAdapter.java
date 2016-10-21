package com.mycompany.kittylogs;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by System User on 10/20/2016.
 */

public class FoodCursorAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;
    private final Context context;

    public FoodCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        this.context = context;
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView brandTextView = (TextView) view.findViewById(R.id.note_text);
  //      TextView flavorTextView = (TextView) view.findViewById
        TextView dateTextView = (TextView) view.findViewById(R.id.note_date);
        String brand = cursor.getString(cursor.getColumnIndex(KittyLogsContract.FoodTable.COLUMN_BRAND));
        String dates = Extras.convertMillisecondsToDate(cursor.getLong(cursor.getColumnIndex(KittyLogsContract.FoodTable.COLUMN_DATE)));
        brandTextView.setText(brand);
        dateTextView.setText(dates);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.from(context).inflate(R.layout.custom_row_view, parent, false);
    }
}
