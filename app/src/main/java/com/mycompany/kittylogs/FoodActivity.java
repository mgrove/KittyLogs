package com.mycompany.kittylogs;

import android.app.AlertDialog;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import static java.lang.System.currentTimeMillis;

public class FoodActivity extends CatDataActivity {
//    DBHelper aHelper;
//    long catID;
    private Cursor aCursor;
    private FoodCursorAdapter aCursorAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   //     setContentView(R.layout.activity_food);
        listView = (ListView) findViewById(R.id.food_list);
        registerForContextMenu(listView);
        loadDataWithCursor();
    }

    protected String getMainTableName(){
        return KittyLogsContract.FoodTable.TABLE_NAME;
    }

    protected String getMainTableColumnCatIDFK(){
        return KittyLogsContract.FoodTable.COLUMN_CAT_IDFK;
    }

    protected int getActivityLayout(){
        return R.layout.activity_food;
    }

    protected String makeTitleString(){
        return "Food for " + aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID);
    }

    private long getCatID(){
        Intent intent = getIntent();
        return intent.getLongExtra(CatProfileActivity.CAT_ID,0);
    }

//    public void setActionBar(){
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        setTitle("Food for " + aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//    }

    public void openAddFoodDialog(View view){
        AlertDialog.Builder addDialogBuilder = new AlertDialog.Builder(this);
        addDialogBuilder.setTitle(R.string.new_food_dialog_title);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText brand = new EditText(this);
        brand.setHint("Brand");
        layout.addView(brand);

        final EditText flavor = new EditText(this);
        flavor.setHint("Flavor");
        layout.addView(flavor);

        final Spinner type = new Spinner(this);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,R.array.food_type_array,android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeAdapter);
        layout.addView(type);

        final Spinner isLikedByCat = new Spinner(this);
        ArrayAdapter<CharSequence> isLikedAdapter = ArrayAdapter.createFromResource(this,R.array.food_is_liked_array,android.R.layout.simple_spinner_item);
        isLikedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        isLikedByCat.setAdapter(isLikedAdapter);
        layout.addView(isLikedByCat);

        addDialogBuilder.setView(layout);
        setAddButtons(addDialogBuilder, brand, flavor, type, isLikedByCat);
        AlertDialog addDialog = addDialogBuilder.create();
        addDialog.show();
    }


    private void setAddButtons(AlertDialog.Builder builder, final EditText brand, final EditText flavor, final Spinner type, final Spinner isLikedByCat){
        builder.setPositiveButton("Add food", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String brandValue = brand.getText().toString();
                String flavorValue = flavor.getText().toString();
                String typeValue = type.getSelectedItem().toString();
                String isLikedValue = isLikedByCat.getSelectedItem().toString();
    //            String value = input.getText().toString();
    //            Log.d("Table name", KittyLogsContract.NotesTable.TABLE_NAME);
                aHelper.addEntryToDB(makeFoodContentValues(brandValue, flavorValue, typeValue, isLikedValue), KittyLogsContract.FoodTable.TABLE_NAME);
                Log.d("Food Table", DatabaseUtils.dumpCursorToString(aHelper.getTableCursorFromDB(KittyLogsContract.FoodTable.TABLE_NAME)));
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

    private ContentValues makeFoodContentValues(String brand, String flavor, String type, String isLiked){
        ContentValues values = new ContentValues();
        values.put(KittyLogsContract.FoodTable.COLUMN_BRAND, brand);
        values.put(KittyLogsContract.FoodTable.COLUMN_FLAVOR, flavor);
        values.put(KittyLogsContract.FoodTable.COLUMN_TYPE, type);
        values.put(KittyLogsContract.FoodTable.COLUMN_IS_LIKED, isLiked);
        values.put(KittyLogsContract.FoodTable.COLUMN_CAT_IDFK, catID);
        values.put(KittyLogsContract.FoodTable.COLUMN_DATE, currentTimeMillis());
        return values;
    }

    protected void loadDataWithCursor(){
        aCursor = aHelper.getTableCursorForCatFromDB(KittyLogsContract.FoodTable.TABLE_NAME, KittyLogsContract.FoodTable.COLUMN_CAT_IDFK, catID);
        aCursorAdapter = new FoodCursorAdapter(this, aCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(aCursorAdapter);
        aHelper.close();
    }

//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_delete, menu);
//    }
//
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        DBHelper aHelper = new DBHelper(getApplicationContext());
//        switch (item.getItemId()) {
//            case R.id.cnt_mnu_delete:
//                makeDeleteDialog(info.id, aHelper);
//                break;
//        }
//        return true;
//    }
//
//    private void makeDeleteDialog(final long rowID, final DBHelper aHelper) {
//        AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(this);
//        makeDeleteMessage(deleteDialogBuilder);
//        setDeleteButtons(deleteDialogBuilder, rowID, aHelper);
//        AlertDialog deleteDialog = deleteDialogBuilder.create();
//        deleteDialog.show();
//    }
//
//    private void makeDeleteMessage(AlertDialog.Builder deleteDialogBuilder){
//        final String deleteMessageString = this.getString(R.string.delete_dialog_message) + " this food?";
//        deleteDialogBuilder.setMessage(deleteMessageString)
//                .setTitle(R.string.delete_dialog_title);
//    }
//
//    private void setDeleteButtons(AlertDialog.Builder deleteDialogBuilder, final long rowID, final DBHelper aHelper){
//        deleteDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                aHelper.removeEntryFromDB(rowID, KittyLogsContract.FoodTable.TABLE_NAME);
//                loadDataWithCursor();
//                return;
//            }
//        });
//        deleteDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                return;
//            }
//        });
//    }

}
