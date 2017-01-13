package com.mycompany.kittylogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Date;

import static java.lang.System.currentTimeMillis;

public class WeightActivity extends CatDataActivity {
    private Cursor aCursor;
    private WeightCursorAdapter aCursorAdapter;
    ListView listView;

    public TimeSeries timeSeries = new TimeSeries("Time Series Title");
    GraphicalView mChartView;
    private XYSeriesRenderer renderer = new XYSeriesRenderer();
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = (ListView) findViewById(R.id.weight_list);
        registerForContextMenu(listView);
        loadDataWithCursor();
        setTabs();
        createChart();
    }

    private void createChart(){
        setTimeSeries();

        mRenderer.setInScroll(true);
        renderer.setLineWidth(2);
        renderer.setColor(Color.RED);
        renderer.setDisplayBoundingPoints(true);
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setPointStrokeWidth(3);
        mRenderer.setYLabelsPadding(10);
        mRenderer.addSeriesRenderer(renderer);
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(timeSeries);
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.chartsRelativeLayout);
        mChartView = ChartFactory.getTimeChartView(this, dataset, mRenderer, null);
        layout.addView(mChartView);
    }

    private void setTimeSeries(){
        aCursor = aHelper.getTableCursorForCatFromDB(KittyLogsContract.WeightTable.TABLE_NAME, KittyLogsContract.WeightTable.COLUMN_CAT_IDFK, catID);
        int count = aCursor.getCount();
        for(int i = 0; i < count; i++){
            aCursor.moveToNext();
            Date thisDate = new Date(aCursor.getLong(1));
            timeSeries.add(thisDate, Double.valueOf(aCursor.getDouble(2)));
        }
        aHelper.close();
    }

    private void repaintChart(){
        timeSeries.clear();
        setTimeSeries();
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.chartsRelativeLayout);
        if (mChartView != null){
            layout.removeView(mChartView);
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(timeSeries);
        mChartView = ChartFactory.getTimeChartView(this, dataset, mRenderer, null);
        layout.addView(mChartView);
        mChartView.repaint();
    }

    protected String getMainTableName(){
         return KittyLogsContract.WeightTable.TABLE_NAME;
    }

    protected String getMainTableColumnCatIDFK(){
        return KittyLogsContract.WeightTable.COLUMN_CAT_IDFK;
    }

    protected int getActivityLayout(){
        return R.layout.activity_weight;
    }

    protected String makeTitleString(){
        return "Weight for " + aHelper.getValueFromDB(KittyLogsContract.CatsTable.COLUMN_CAT_NAME, KittyLogsContract.CatsTable.TABLE_NAME, KittyLogsContract.CatsTable._ID, catID);
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

    public void openAddWeightDialog(View view) {
        AlertDialog.Builder addDialogBuilder = new AlertDialog.Builder(this);
        addDialogBuilder.setTitle(R.string.new_weight_dialog_title);
        LayoutInflater inflater = getLayoutInflater();
        addDialogBuilder.setView(inflater.inflate(R.layout.add_weight_dialog, null));
        setAddButtons(addDialogBuilder);
        AlertDialog addDialog = addDialogBuilder.create();
        addDialog.show();
    }

    private void setAddButtons(AlertDialog.Builder builder) {
        builder.setPositiveButton("Add weight", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Dialog addDialog = (Dialog) dialog;
                EditText input = (EditText)addDialog.findViewById(R.id.weight_input);
                String value = input.getText().toString();
                aHelper.addEntryToDB(makeWeightContentValues(value), KittyLogsContract.WeightTable.TABLE_NAME);
                loadDataWithCursor();
                repaintChart();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
    }

    private ContentValues makeWeightContentValues(String entry) {
        ContentValues values = new ContentValues();
        values.put(KittyLogsContract.WeightTable.COLUMN_WEIGHT, entry);
        values.put(KittyLogsContract.WeightTable.COLUMN_CAT_IDFK, catID);
        values.put(KittyLogsContract.WeightTable.COLUMN_DATE, currentTimeMillis());
        return values;
    }

    protected void loadDataWithCursor(){
        aCursor = aHelper.getTableCursorForCatFromDB(KittyLogsContract.WeightTable.TABLE_NAME, KittyLogsContract.WeightTable.COLUMN_CAT_IDFK, catID);
        aCursorAdapter = new WeightCursorAdapter(this, aCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(aCursorAdapter);
        aHelper.close();
    }

    @Override
    protected void deleteEntryWithMenu(final long rowID){
        super.deleteEntryWithMenu(rowID);
        repaintChart();
    }

}
