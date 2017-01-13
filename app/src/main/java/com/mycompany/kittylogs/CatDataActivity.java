package com.mycompany.kittylogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

public abstract class CatDataActivity extends AppCompatActivity {
    DBHelper aHelper;
    long catID;
    protected int activityLayout;
    protected String mainTableName;
    protected String mainTableColumnCatIDFK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVariables();
        setContentView(activityLayout);
        setActionBar();
    }

    protected void setVariables() {
        aHelper = new DBHelper(getApplicationContext());
        catID = getCatID();
        activityLayout = getActivityLayout();
        mainTableName = getMainTableName();
        Log.d("Main table name:", mainTableName);
        mainTableColumnCatIDFK = getMainTableColumnCatIDFK();
        Log.d("catID from setVariables", Long.toString(catID));
    }

    private long getCatID() {
        Intent intent = getIntent();
        return intent.getLongExtra(CatProfileActivity.CAT_ID, 0);
    }

    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(makeTitleString());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
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
        final String deleteMessageString = this.getString(R.string.delete_dialog_message) + " this?";
        deleteDialogBuilder.setMessage(deleteMessageString)
                .setTitle(R.string.delete_dialog_title);
    }

    private void setDeleteButtons(AlertDialog.Builder deleteDialogBuilder, final long rowID, final DBHelper aHelper){
        deleteDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
//                aHelper.removeEntryFromDB(rowID, mainTableName);
//                loadDataWithCursor();
                deleteEntryWithMenu(rowID);
            }
        });
        deleteDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id){}
        });
    }

    protected void deleteEntryWithMenu(final long rowID){
        aHelper.removeEntryFromDB(rowID, mainTableName);
        loadDataWithCursor();
    }

    protected abstract void loadDataWithCursor();

    protected abstract String makeTitleString();

    protected abstract String getMainTableName();

    protected abstract String getMainTableColumnCatIDFK();

    protected abstract int getActivityLayout();
}
