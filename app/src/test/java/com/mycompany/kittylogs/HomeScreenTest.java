package com.mycompany.kittylogs;

import android.content.ContentValues;
import android.util.Log;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by System User on 11/14/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class HomeScreenTest {

    @Mock
    HomeScreen aHomeScreen = new HomeScreen();

    ContentValues values;


    @Test
    public void onCreate() throws Exception {

    }

    @Ignore
    @Test
    public void makeCatContentValues() throws Exception {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name","Flurry");
        ContentValues resultValues = aHomeScreen.makeCatContentValues("Flurry");
        Log.d("Result Error", resultValues.toString());
        String result = resultValues.getAsString(KittyLogsContract.CatsTable.COLUMN_CAT_NAME);
        assertThat("Test content values", "Flurry", is(result));

    }

    @Test
    public void onCreateContextMenu() throws Exception {

    }

    @Test
    public void onContextItemSelected() throws Exception {

    }

    @Test
    public void onItemClick() throws Exception {

    }

    @Test
    public void onCreateOptionsMenu() throws Exception {

    }

    @Test
    public void onOptionsItemSelected() throws Exception {

    }

//    private void createMockHomeScreen(){
//        values = new ContentValues();
//        values.put("name", "Flurry");
//        when(mockHomeScreen.makeCatContentValues("Flurry")).thenReturn(values);
//    }

}