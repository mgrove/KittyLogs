package com.mycompany.kittylogs;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import static com.mycompany.kittylogs.R.id.cat_list;


public class HomeScreen extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public final static String CLICKED_CAT = "com.example.kittylogs.CAT_NAME";
    ListView listView;
    Button btnAdd;
    EditText inputLabel;
    private static final int TEXT_ID = 0;
    private Cursor aCursor;
    KLCursorAdapter aCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home_screen);
        populateListView();
        loadDataWithCursor();
        startCreateButtonAndInput();
    }

    private void populateListView(){
        listView = (ListView) findViewById(cat_list);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(this);
    }

    private void startCreateButtonAndInput(){
        btnAdd = (Button) findViewById(R.id.add_cat_button);
        inputLabel = (EditText) findViewById(R.id.edit_message);
        startClickListener();
    }

    private void startClickListener(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String catText = inputLabel.getText().toString();
                if (catText.trim().length() > 0) {
                    addCat(catText);
                    loadDataWithCursor();
                } else {
                    Toast.makeText(getApplicationContext(), "Enter new cat name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addCat(String text){
        DBHelper aHelper = new DBHelper(getApplicationContext());
        aHelper.addEntryToDB(makeCatContentValues(text), KittyLogsContract.CatsTable.TABLE_NAME);
        inputLabel.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputLabel.getWindowToken(), 0);
    }

    public ContentValues makeCatContentValues(String name){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KittyLogsContract.CatsTable.COLUMN_CAT_NAME,name);
        return contentValues;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_actions, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        DBHelper aHelper = new DBHelper(getApplicationContext());
        switch (item.getItemId()) {
            case R.id.cnt_mnu_edit:
                makeEditDialog(info.id, aHelper);
                break;
            case R.id.cnt_mnu_delete:
                makeDeleteDialog(info.id, aHelper);
                break;
        }
        return true;
    }

    private void makeDeleteDialog(final long rowID, final DBHelper aHelper) {
        AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(this);
        makeDeleteMessage(deleteDialogBuilder, rowID, aHelper);
        setDeleteButtons(deleteDialogBuilder, rowID, aHelper);
        AlertDialog deleteDialog = deleteDialogBuilder.create();
        deleteDialog.show();
    }

    private void makeDeleteMessage(AlertDialog.Builder deleteDialogBuilder, long rowID, DBHelper aHelper){
        final String deleteMessageString = this.getString(R.string.delete_dialog_message) + " " + aHelper.getValueFromDB(
                KittyLogsContract.CatsTable.COLUMN_CAT_NAME,
                KittyLogsContract.CatsTable.TABLE_NAME,
                KittyLogsContract.CatsTable._ID, rowID) + "?";
        deleteDialogBuilder.setMessage(deleteMessageString)
                .setTitle(R.string.delete_dialog_title);
    }

    private void setDeleteButtons(AlertDialog.Builder deleteDialogBuilder, final long rowID, final DBHelper aHelper){
        deleteDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                aHelper.removeCatFromDB(rowID);
                loadDataWithCursor();
            }
        });
        deleteDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id){}
        });
    }

    private void makeEditDialog(final long rowID, final DBHelper aHelper) {
        AlertDialog.Builder editDialogBuilder = new AlertDialog.Builder(this);
        editDialogBuilder.setMessage(R.string.edit_dialog_message)
                .setTitle(R.string.edit_dialog_title);
        final EditText input = new EditText(this);
 //       input.setId(TEXT_ID); (I'm not sure if this line does anything)
        editDialogBuilder.setView(input);
        setEditButtons(editDialogBuilder, input, rowID, aHelper);
        AlertDialog editDialog = editDialogBuilder.create();
        editDialog.show();
    }

    private void setEditButtons(AlertDialog.Builder editDialogBuilder, final EditText input, final long rowID, final DBHelper aHelper){
        editDialogBuilder.setPositiveButton(R.string.str_cnt_mnu_edit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String value = input.getText().toString();
                aHelper.editEntryInDB(makeCatContentValues(value), rowID, KittyLogsContract.CatsTable.TABLE_NAME);
                loadDataWithCursor();
            }
        });
        editDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
    }

    private void loadDataWithCursor() {
        DBHelper aHelper = new DBHelper(getApplicationContext());
        aCursor = aHelper.getTableCursorFromDB(KittyLogsContract.CatsTable.TABLE_NAME);
        aCursorAdapter = new KLCursorAdapter(this, aCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(aCursorAdapter);
        aHelper.close();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, CatProfileActivity.class);
        intent.putExtra(CLICKED_CAT,id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class KLCursorAdapter extends android.support.v4.widget.CursorAdapter {
        private LayoutInflater cursorInflater;

        protected KLCursorAdapter(Context context, Cursor cursor, int flags){
            super(context,cursor,flags);
            cursorInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor){
            TextView catTextView = (TextView)view.findViewById(R.id.rowTextView);
            String cats = cursor.getString(cursor.getColumnIndex(KittyLogsContract.CatsTable.COLUMN_CAT_NAME));
            catTextView.setText(cats);
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent){
            return cursorInflater.from(context).inflate(R.layout.cat_list_text_view, parent, false);
        }
    }
}
