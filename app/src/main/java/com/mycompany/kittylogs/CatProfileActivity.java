package com.mycompany.kittylogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CatProfileActivity extends LaunchActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeNameView(catName);
    }

    protected long getCatID(){
        Intent intent = getIntent();
        return intent.getLongExtra(HomeScreen.CLICKED_CAT, 0);
    }

    protected String makeTitleString(){
        return catName;
    }

    protected int getActivityLayout(){
        return R.layout.activity_cat_profile;
    }

    private void makeNameView(String catName){
        textView = (TextView)findViewById(R.id.cat_name);
        textView.setTextSize(40);
        textView.setText(catName);
    }

    public void startNotesActivity(View view){
        startClassActivity(NotesActivity.class);
    }

    public void startFoodActivity(View view){
        startClassActivity(FoodActivity.class);
    }

    public void startWeightActivity(View view){
        startClassActivity(WeightActivity.class);
    }

    public void startMedicalRecordsActivity(View view){
        startClassActivity(MedicalRecordsActivity.class);
    }

}
