package com.mycompany.kittylogs;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.List;

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
        listView = (ListView) findViewById(cat_list);

        registerForContextMenu(listView);

        btnAdd = (Button) findViewById(R.id.add_cat_button);
        inputLabel = (EditText) findViewById(R.id.edit_message);
        listView.setOnItemClickListener(this);
        loadDataWithCursor();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String catText = inputLabel.getText().toString();
                if (catText.trim().length() > 0) {
                    DBHelper aHelper = new DBHelper(getApplicationContext());
                    aHelper.addEntryToDB(makeCatContentValues(catText), KittyLogsContract.CatsTable.TABLE_NAME);
                    inputLabel.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputLabel.getWindowToken(), 0);
                    loadDataWithCursor();
                } else {
                    Toast.makeText(getApplicationContext(), "Enter new cat", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public ContentValues makeCatContentValues(String name){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KittyLogsContract.CatsTable.COLUMN_CAT_NAME,name);
        return contentValues;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
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
        AlertDialog.Builder editDialogBuilder = new AlertDialog.Builder(this);
        final String deleteMessageString = this.getString(R.string.delete_dialog_message) + " " + aHelper.getCatNameFromDB((rowID)) + "?";
        editDialogBuilder.setMessage(deleteMessageString)
                .setTitle(R.string.delete_dialog_title);
        editDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                aHelper.removeEntryFromDB(rowID, KittyLogsContract.CatsTable.TABLE_NAME);
                loadDataWithCursor();
                return;
            }
        });
        editDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        AlertDialog editDialog = editDialogBuilder.create();
        editDialog.show();
    }

    private void makeEditDialog(final long rowID, final DBHelper aHelper) {
        AlertDialog.Builder editDialogBuilder = new AlertDialog.Builder(this);
        editDialogBuilder.setMessage(R.string.edit_dialog_message)
                .setTitle(R.string.edit_dialog_title);
        final EditText input = new EditText(this);
        input.setId(TEXT_ID);
        editDialogBuilder.setView(input);
        editDialogBuilder.setPositiveButton(R.string.str_cnt_mnu_edit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String value = input.getText().toString();
                aHelper.editCatInDB(rowID, value);
                loadDataWithCursor();
                return;
            }
        });
        editDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });

        AlertDialog editDialog = editDialogBuilder.create();
        editDialog.show();
    }

    private void loadDataWithCursor() {
        DBHelper aHelper = new DBHelper(getApplicationContext());
        aCursor = aHelper.getCatsCursorFromDB();
        aCursorAdapter = new KLCursorAdapter(this, aCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(aCursorAdapter);
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
}
