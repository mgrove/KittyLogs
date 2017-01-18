package com.mycompany.kittylogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import static java.lang.System.currentTimeMillis;

public class FoodActivity extends CatDataActivity {
    private Cursor aCursor;
    private FoodCursorAdapter aCursorAdapter;
    ListView listView;
    Context context = this;

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

    public void openAddFoodDialog(View view){
        AlertDialog.Builder addDialogBuilder = new AlertDialog.Builder(this);
        addDialogBuilder.setTitle(R.string.new_food_dialog_title);

//        LinearLayout layout = new LinearLayout(this);
//        layout.setOrientation(LinearLayout.VERTICAL);
//
//        final EditText brand = new EditText(this);
//        brand.setHint("Brand");
//        layout.addView(brand);
//
//        final EditText flavor = new EditText(this);
//        flavor.setHint("Flavor");
//        layout.addView(flavor);
//
//        final Spinner type = (Spinner)addDialog.findViewById(R.id.type_spinner);
//        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,R.array.food_type_array,android.R.layout.simple_spinner_item);
//        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        type.setAdapter(typeAdapter);
//        layout.addView(type);
//
//        final Spinner isLikedByCat = (Spinner)addDialog.findViewById(R.id.liked_spinner);
//        ArrayAdapter<CharSequence> isLikedAdapter = ArrayAdapter.createFromResource(this,R.array.food_is_liked_array,android.R.layout.simple_spinner_item);
//        isLikedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        isLikedByCat.setAdapter(isLikedAdapter);
//        layout.addView(isLikedByCat);
//
//        addDialogBuilder.setView(layout);
//        setAddButtons(addDialogBuilder, brand, flavor, type, isLikedByCat);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.add_food_dialog, null);
        addDialogBuilder.setView(dialogLayout);

        final Spinner type = (Spinner)dialogLayout.findViewById(R.id.type_spinner);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,R.array.food_type_array,android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeAdapter);

        final Spinner isLikedByCat = (Spinner)dialogLayout.findViewById(R.id.liked_spinner);
        ArrayAdapter<CharSequence> isLikedAdapter = ArrayAdapter.createFromResource(this,R.array.food_is_liked_array,android.R.layout.simple_spinner_item);
        isLikedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        isLikedByCat.setAdapter(isLikedAdapter);

        setAddButtons(addDialogBuilder, type, isLikedByCat);
        AlertDialog addDialog = addDialogBuilder.create();
        addDialog.show();
    }

    private void setAddButtons(AlertDialog.Builder builder /*, final EditText brand, final EditText flavor*/, final Spinner type, final Spinner isLikedByCat){
        builder.setPositiveButton("Add food", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Dialog addDialog = (Dialog) dialog;
                EditText brand = (EditText)addDialog.findViewById(R.id.brand_input);
                EditText flavor = (EditText)addDialog.findViewById(R.id.flavor_input);

//            //    Spinner type = (Spinner)addDialog.findViewById(R.id.type_spinner);
//                ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(context,R.array.food_type_array,android.R.layout.simple_spinner_item);
//                typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                type.setAdapter(typeAdapter);
//
//             //   Spinner isLikedByCat = (Spinner)addDialog.findViewById(R.id.liked_spinner);
//                ArrayAdapter<CharSequence> isLikedAdapter = ArrayAdapter.createFromResource(context,R.array.food_is_liked_array,android.R.layout.simple_spinner_item);
//                isLikedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                isLikedByCat.setAdapter(isLikedAdapter);

                String brandValue = brand.getText().toString();
                String flavorValue = flavor.getText().toString();
                String typeValue = type.getSelectedItem().toString();
                String isLikedValue = isLikedByCat.getSelectedItem().toString();
    //            String value = input.getText().toString();
    //            Log.d("Table name", KittyLogsContract.NotesTable.TABLE_NAME);
                aHelper.addEntryToDB(makeFoodContentValues(brandValue, flavorValue, typeValue, isLikedValue), KittyLogsContract.FoodTable.TABLE_NAME);
                Log.d("Food Table", DatabaseUtils.dumpCursorToString(aHelper.getTableCursorFromDB(KittyLogsContract.FoodTable.TABLE_NAME)));
                loadDataWithCursor();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
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

    public class FoodCursorAdapter extends android.support.v4.widget.CursorAdapter {

        private LayoutInflater cursorInflater;
        private final Context context;

        protected FoodCursorAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
            this.context = context;
            cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor) {
            TextView brandTextView = (TextView) view.findViewById(R.id.food_brand);
            TextView flavorTextView = (TextView) view.findViewById(R.id.food_flavor);
            TextView typeTextView = (TextView) view.findViewById(R.id.food_type);
            TextView dateTextView = (TextView) view.findViewById(R.id.food_date);
            TextView likedTextView = (TextView) view.findViewById(R.id.food_liked);
            String brand = cursor.getString(cursor.getColumnIndex(KittyLogsContract.FoodTable.COLUMN_BRAND));
            String flavor = cursor.getString(cursor.getColumnIndex(KittyLogsContract.FoodTable.COLUMN_FLAVOR));
            String type = "Type: " + cursor.getString(cursor.getColumnIndex(KittyLogsContract.FoodTable.COLUMN_TYPE));
            String dates = "Date added: " + Extras.convertMillisecondsToDate(cursor.getLong(cursor.getColumnIndex(KittyLogsContract.FoodTable.COLUMN_DATE)));
            String liked = "Liked by cat? " + cursor.getString(cursor.getColumnIndex(KittyLogsContract.FoodTable.COLUMN_IS_LIKED));
            brandTextView.setText(brand);
            flavorTextView.setText(flavor);
            typeTextView.setText(type);
            dateTextView.setText(dates);
            likedTextView.setText(liked);
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return cursorInflater.from(context).inflate(R.layout.food_row_view, parent, false);
        }
    }

}
