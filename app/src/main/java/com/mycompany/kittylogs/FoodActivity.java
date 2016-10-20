package com.mycompany.kittylogs;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import static java.lang.System.currentTimeMillis;

public class FoodActivity extends AppCompatActivity {
    DBHelper aHelper;
    long catID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        aHelper = new DBHelper(getApplicationContext());
        catID = getCatID();
        setActionBar();
        setFloatingActionButton();
    }

    private long getCatID(){
        Intent intent = getIntent();
        return intent.getLongExtra(CatProfileActivity.CAT_ID,0);
    }

    public void setActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Food for " + aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setFloatingActionButton(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.hide();
    }

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
        builder.setPositiveButton("Add note", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String brandValue = brand.getText().toString();
                String flavorValue = flavor.getText().toString();
                String typeValue = type.getSelectedItem().toString();
                String isLikedValue = isLikedByCat.getSelectedItem().toString();
    //            String value = input.getText().toString();
    //            Log.d("Table name", KittyLogsContract.NotesTable.TABLE_NAME);
                aHelper.addEntryToDB(makeFoodContentValues(brandValue, flavorValue, typeValue, isLikedValue), KittyLogsContract.FoodTable.TABLE_NAME);
                Log.d("Food Table", DatabaseUtils.dumpCursorToString(aHelper.getTableCursorFromDB(KittyLogsContract.FoodTable.TABLE_NAME)));
  //              loadDataWithCursor();
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

}
