package com.mycompany.kittylogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
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
import android.widget.RelativeLayout;
import android.widget.TabHost;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Date;

import static com.mycompany.kittylogs.R.id.chartsRelativeLayout;
import static java.lang.System.currentTimeMillis;

public class WeightActivity extends CatDataActivity {
    private Cursor aCursor;
    private WeightCursorAdapter aCursorAdapter;
    //  TableLayout tableLayout;
    ListView listView;

    public TimeSeries timeSeries = new TimeSeries("Time Series Title");
    GraphicalView mChartView;
    private XYSeriesRenderer renderer = new XYSeriesRenderer();
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private CategorySeries mSeries = new CategorySeries("Expenses");
    private static int[] COLORS = new int[] {
            Color.GREEN, Color.BLUE, Color.MAGENTA, Color.YELLOW, Color.RED, Color.DKGRAY, Color.BLACK};

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
        Date date1 = new Date();
        Long long1 = new Long(2);
        timeSeries.add(date1, long1);
        Date date2 = new Date();
        Long long2 = new Long(5);
        timeSeries.add(date2, long2);
        renderer.setLineWidth(2);
        renderer.setColor(Color.RED);
        renderer.setDisplayBoundingPoints(true);
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setPointStrokeWidth(3);
        mRenderer.setYLabelsPadding(10);
        mRenderer.addSeriesRenderer(renderer);
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(timeSeries);

        mChartView = ChartFactory.getTimeChartView(this, dataset, mRenderer, null);
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.chartsRelativeLayout);
        layout.addView(mChartView);
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
                Log.d("Weight Table", DatabaseUtils.dumpCursorToString(aHelper.getTableCursorFromDB(KittyLogsContract.WeightTable.TABLE_NAME)));
                loadDataWithCursor();
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

}
