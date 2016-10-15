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
    private final Context context;

    public NotesCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        this.context = context;
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }

//    @Override
//    public View getView(int position, View view, ViewGroup parent){
//
//
//        TextView dateView = (TextView) rowView.findViewById(R.id.note_date);
//        TextView contentView = (TextView) rowView.findViewById(R.id.note_text);
//
//    }

    public void bindView(View view, Context context, Cursor cursor) {
   //     LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
  //      view = inflater.inflate(R.layout.custom_row_view, null, true);
        TextView noteTextView = (TextView) view.findViewById(R.id.note_text);
        TextView dateTextView = (TextView) view.findViewById(R.id.note_date);
        String notes = cursor.getString(cursor.getColumnIndex(KittyLogsContract.NotesTable.COLUMN_ENTRY));
        noteTextView.setText(notes);
        dateTextView.setText("dates");

    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.from(context).inflate(R.layout.custom_row_view, parent, false);
    }
}
