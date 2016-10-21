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
        TextView brandTextView = (TextView) view.findViewById(R.id.food_brand);
        TextView flavorTextView = (TextView) view.findViewById(R.id.food_flavor);
        TextView typeTextView = (TextView) view.findViewById(R.id.food_type);
        TextView dateTextView = (TextView) view.findViewById(R.id.food_date);
        TextView likedTextView = (TextView) view.findViewById(R.id.food_liked);
        String brand = cursor.getString(cursor.getColumnIndex(KittyLogsContract.FoodTable.COLUMN_BRAND));
        String flavor = cursor.getString(cursor.getColumnIndex(KittyLogsContract.FoodTable.COLUMN_FLAVOR));
        String type = cursor.getString(cursor.getColumnIndex(KittyLogsContract.FoodTable.COLUMN_TYPE));
        String dates = Extras.convertMillisecondsToDate(cursor.getLong(cursor.getColumnIndex(KittyLogsContract.FoodTable.COLUMN_DATE)));
        String liked = cursor.getString(cursor.getColumnIndex(KittyLogsContract.FoodTable.COLUMN_IS_LIKED));
        brandTextView.setText(brand);
        flavorTextView.setText(flavor);
        typeTextView.setText(type);
        dateTextView.setText(dates);
        likedTextView.setText(liked);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.from(context).inflate(R.layout.food_row_view, parent, false);
    }
}
