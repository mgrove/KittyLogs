package com.mycompany.kittylogs;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.content.Context;
        import android.database.Cursor;
        import android.support.v7.app.ActionBarActivity;
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


public class HomeScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public final static String NEW_CAT = "com.example.kittylogs.CAT_NAME";
    ListView listView;
    Button btnAdd;
    EditText inputLabel;

    private Cursor aCursor;
    KLCursorAdapter aCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        listView = (ListView) findViewById(cat_list);



        registerForContextMenu(listView);

        btnAdd = (Button) findViewById(R.id.add_cat_button);
        inputLabel = (EditText) findViewById(R.id.edit_message);
        listView.setOnItemSelectedListener(this);
        loadDataWithCursor();
        btnAdd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                String catText = inputLabel.getText().toString();
                if (catText.trim().length() > 0){
                    DBHelper aHelper = new DBHelper(getApplicationContext());
                    aHelper.addCatToDB(catText);
                    inputLabel.setText("");
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputLabel.getWindowToken(), 0);
                    loadDataWithCursor();
                } else {
                    Toast.makeText(getApplicationContext(), "Enter new cat", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
    }

    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        DBHelper aHelper = new DBHelper(getApplicationContext());
        switch(item.getItemId()){
            case R.id.cnt_mnu_edit:

                break;
            case R.id.cnt_mnu_delete:
                aHelper.removeCatFromDB(info.id);
                aHelper.removeCatFromDB(aCursorAdapter.getItemId((int)info.id));
                loadDataWithCursor();
                break;
        }
        return true;
    }

    private void loadDataWithCursor(){
        DBHelper aHelper = new DBHelper(getApplicationContext());
        aCursor = aHelper.getCatsCursorFromDB();
        aCursorAdapter = new KLCursorAdapter(this, aCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(aCursorAdapter);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        //       String label = parent.getItemAtPosition(position).toString();
        //       Toast.makeText
    }

    public void onNothingSelected(AdapterView<?> arg0){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    public void createCat(View view){
        EditText editText = (EditText)findViewById(R.id.edit_message);
        String catName = editText.getText().toString();
        DBHelper aHelper = new DBHelper(this);
        aHelper.addCatToDB(catName);
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
