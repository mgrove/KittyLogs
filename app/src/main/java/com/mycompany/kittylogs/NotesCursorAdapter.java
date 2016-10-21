package com.mycompany.kittylogs;

import android.content.Context;
import android.database.Cursor;
//import android.icu.util.Calendar;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by System User on 10/11/2016.
 */

public class NotesCursorAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;
    private final Context context;


    public NotesCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        this.context = context;
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView noteTextView = (TextView) view.findViewById(R.id.note_text);
        TextView dateTextView = (TextView) view.findViewById(R.id.note_date);
        String notes = cursor.getString(cursor.getColumnIndex(KittyLogsContract.NotesTable.COLUMN_ENTRY));
        String dates = Extras.convertMillisecondsToDate(cursor.getLong(cursor.getColumnIndex(KittyLogsContract.NotesTable.COLUMN_DATE)));
        noteTextView.setText(notes);
        dateTextView.setText(dates);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.from(context).inflate(R.layout.note_row_view, parent, false);
    }
}
