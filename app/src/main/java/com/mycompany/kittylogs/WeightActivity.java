package com.mycompany.kittylogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TableLayout;

import static java.lang.System.currentTimeMillis;

public class WeightActivity extends AppCompatActivity {
    DBHelper aHelper;
    long catID;
    private Cursor aCursor;
    private WeightCursorAdapter aCursorAdapter;
    //  TableLayout tableLayout;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        aHelper = new DBHelper(getApplicationContext());
        catID = getCatID();
        setActionBar();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.weight_list);
        registerForContextMenu(listView);
        loadDataWithCursor();
        setTabs();
    }

    private void setTabs(){
        TabHost host = (TabHost)findViewById(R.id.tab_host);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("Chart View");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Chart View");
        host.addTab(spec);
        
        spec = host.newTabSpec("List View");
        spec.setContent(R.id.tab2);
        spec.setIndicator("List View");
        host.addTab(spec);
    }

    private long getCatID() {
        Intent intent = getIntent();
        return intent.getLongExtra(CatProfileActivity.CAT_ID, 0);
    }

    public void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Weight for " + aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void openAddWeightDialog(View view) {
        AlertDialog.Builder addDialogBuilder = new AlertDialog.Builder(this);
        addDialogBuilder.setTitle(R.string.new_note_dialog_title);
        LayoutInflater inflater = getLayoutInflater();
        addDialogBuilder.setView(inflater.inflate(R.layout.add_weight_dialog, null));
        setAddButtons(addDialogBuilder);
        AlertDialog addDialog = addDialogBuilder.create();
        addDialog.show();
    }

    private void setAddButtons(AlertDialog.Builder builder) {
        builder.setPositiveButton("Add note", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Dialog addDialog = (Dialog) dialog;
                EditText input = (EditText)addDialog.findViewById(R.id.weight_input);
                String value = input.getText().toString();
                aHelper.addEntryToDB(makeWeightContentValues(value), KittyLogsContract.WeightTable.TABLE_NAME);
                Log.d("Weight Table", DatabaseUtils.dumpCursorToString(aHelper.getTableCursorFromDB(KittyLogsContract.WeightTable.TABLE_NAME)));
                loadDataWithCursor();
                return;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
    }

    private ContentValues makeWeightContentValues(String entry) {
        ContentValues values = new ContentValues();
        values.put(KittyLogsContract.WeightTable.COLUMN_WEIGHT, entry);
        values.put(KittyLogsContract.WeightTable.COLUMN_CAT_IDFK, catID);
        values.put(KittyLogsContract.WeightTable.COLUMN_DATE, currentTimeMillis());
        return values;
    }

    private void loadDataWithCursor(){
        aCursor = aHelper.getTableCursorForCatFromDB(KittyLogsContract.WeightTable.TABLE_NAME, KittyLogsContract.WeightTable.COLUMN_CAT_IDFK, catID);
        aCursorAdapter = new WeightCursorAdapter(this, aCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(aCursorAdapter);
        aHelper.close();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.food_actions, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        DBHelper aHelper = new DBHelper(getApplicationContext());
        switch (item.getItemId()) {
            case R.id.cnt_mnu_delete:
                makeDeleteDialog(info.id, aHelper);
                break;
        }
        return true;
    }

    private void makeDeleteDialog(final long rowID, final DBHelper aHelper) {
        AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(this);
        makeDeleteMessage(deleteDialogBuilder);
        setDeleteButtons(deleteDialogBuilder, rowID, aHelper);
        AlertDialog deleteDialog = deleteDialogBuilder.create();
        deleteDialog.show();
    }

    private void makeDeleteMessage(AlertDialog.Builder deleteDialogBuilder){
        final String deleteMessageString = this.getString(R.string.delete_dialog_message) + " this weight?";
        deleteDialogBuilder.setMessage(deleteMessageString)
                .setTitle(R.string.delete_dialog_title);
    }

    private void setDeleteButtons(AlertDialog.Builder deleteDialogBuilder, final long rowID, final DBHelper aHelper){
        deleteDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                aHelper.removeEntryFromDB(rowID, KittyLogsContract.WeightTable.TABLE_NAME);
                loadDataWithCursor();
                return;
            }
        });
        deleteDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
    }

}
