package com.mycompany.kittylogs;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by System User on 10/11/2016.
 */

public class NotesCursorAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;

    public NotesCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView noteTextView = (TextView) view.findViewById(R.id.rowTextView);
        String notes = cursor.getString(cursor.getColumnIndex(KittyLogsContract.NotesTable.COLUMN_ENTRY));
        noteTextView.setText(notes);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.from(context).inflate(R.layout.list_text_view, parent, false);
    }
}
