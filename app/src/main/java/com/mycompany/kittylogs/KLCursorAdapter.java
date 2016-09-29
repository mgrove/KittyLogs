package com.mycompany.kittylogs;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.CursorAdapter;

/**
 * Created by System User on 9/27/2016.
 */

public class KLCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;

    public KLCursorAdapter(Context context, Cursor cursor, int flags){
        super(context,cursor,flags);
        cursorInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindView(View view, Context context, Cursor cursor){
        TextView textViewTitle = (TextView)view.findViewById(R.id.rowTextView);
        String title = cursor.getString(cursor.getColumnIndex(KittyLogsContract.CatsTable.COLUMN_CAT_NAME));
        textViewTitle.setText(title);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return cursorInflater.from(context).inflate(R.layout.list_text_view, parent, false);
    }
}
