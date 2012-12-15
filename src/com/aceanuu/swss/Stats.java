package com.aceanuu.swss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.aceanuu.swss.sqlite.DatabaseManager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;


public class Stats extends SherlockActivity{

    private GraphicalView mChartView;
    boolean finished_starting = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.xy_chart);
        
        
//        TabWidget tab = (TabWidget) findViewById(R.id.tabs);
        TabHost tab_host = (TabHost) findViewById(android.R.id.tabhost);
        TabHost.TabSpec ts;
        tab_host.setup();
        ts = tab_host.newTabSpec("Players").setIndicator("Players").setContent(R.id.player_tab);
        tab_host.addTab(ts);
        ts = tab_host.newTabSpec("Games").setIndicator("Games").setContent(R.id.chart2);
        tab_host.addTab(ts);
        ts = tab_host.newTabSpec("Categories").setIndicator("Categories").setContent(R.id.chart);
        tab_host.addTab(ts);
        
        tab_host.setCurrentTab(0);
        
        final ActionBar ab = getSupportActionBar();
//        ab.setTitle("Score Sheet");
        ab.setDisplayShowTitleEnabled(false);
        
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add(this.getResources().getString(R.string.ab_modes_scoresheet));
        modes.add(this.getResources().getString(R.string.ab_modes_stats));
        ArrayAdapter<String> aa = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                modes);
        ab.setListNavigationCallbacks(aa, new ActionBar.OnNavigationListener() {
            
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                
                if(!finished_starting) 
                {
                    finished_starting = true;
                    return false;
                }
                
                if(itemPosition == 0) //if other thing
                {
                    startScore(); 
                    return true;
                } 
                return false;
            }
    
        });
        ab.setSelectedNavigationItem(1);
        
        
        List<double[]> values = new ArrayList<double[]>();
        values.add(new double[] { 12, 14, 11, 10, 19, 12, 14, 11, 10 });
        
        List<String[]> titles = new ArrayList<String[]>();
        titles.add(new String[] { "Military", "Money", "Wonder", "Civilian", "Commercial", "Science", "Guild", "Leader", "Cities" });
        int[] colors = new int[] { 
        getResources().getColor(R.color.military)
        ,getResources().getColor(R.color.money)
        ,getResources().getColor(R.color.wonder)
        ,getResources().getColor(R.color.civilian)
        ,getResources().getColor(R.color.commercial)
        ,getResources().getColor(R.color.science)
        ,getResources().getColor(R.color.guild)
        ,getResources().getColor(R.color.leader)
        ,getResources().getColor(R.color.cities) };
        
        DefaultRenderer renderer = buildCategoryRenderer(colors);
        renderer.setLabelsColor(Color.BLACK);

        LinearLayout layout = (LinearLayout) findViewById(R.id.chart);

//        mChartView = ChartFactory.getDoughnutChartView(getApplicationContext(), buildMultipleCategoryDataset("Project budget", titles, values), renderer);
        
        mChartView = ChartFactory.getDoughnutChartView(this.getApplicationContext(),
            buildMultipleCategoryDataset("Project budget", titles, values), renderer);
        
        layout.addView(mChartView, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

         layout = (LinearLayout) findViewById(R.id.chart2);
        mChartView = ChartFactory.getDoughnutChartView(this.getApplicationContext(),
            buildMultipleCategoryDataset("Project budget", titles, values), renderer);

        layout.addView(mChartView, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        
        Spinner pspinner = (Spinner) findViewById(R.id.tab_player_select_player);
        
        
        final DatabaseManager db = new DatabaseManager(this);
        
        final HashMap<String, Integer> pidmap = db.getPlayerNamePIDs();        
        final ArrayList<String>  names = new ArrayList<String>(pidmap.keySet());
        ArrayAdapter<String> spinneradpater = new ArrayAdapter<String>(
                                                  this,
                                                  android.R.layout.simple_spinner_dropdown_item,
                                                  names);
        pspinner.setAdapter(spinneradpater);
        
        final TextView info = (TextView) findViewById(R.id.tab_player_info);
        
        pspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int position, long arg3) {
                String infos;
                
                int pid = pidmap.get(names.get(position));
                
                infos = db.getPlayerHistory(names.get(position), pid).toString();
                info.setText(infos);
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                
            }
            
        });
        
    }

    /**
     * Builds a multiple category series using the provided values.
     * 
     * @param titles the series titles
     * @param values the values
     * @return the category series
     */
    protected MultipleCategorySeries buildMultipleCategoryDataset(String title,
        List<String[]> titles, List<double[]> values) {
      MultipleCategorySeries series = new MultipleCategorySeries(title);
      int k = 0;
      for (double[] value : values) {
        series.add(2007 + k + "", titles.get(k), value);
        k++;
      }
      return series;
    }
    

    /**
     * Builds a category renderer to use the provided colors.
     * 
     * @param colors the colors
     * @return the category renderer
     */
    protected DefaultRenderer buildCategoryRenderer(int[] colors) {
      DefaultRenderer renderer = new DefaultRenderer();
      renderer.setLabelsTextSize(25);
      renderer.setShowLegend(false);
      renderer.setZoomEnabled(false);
      renderer.setExternalZoomEnabled(false);
      renderer.setPanEnabled(false);
      renderer.setMargins(new int[] { 20, 30, 15, 0 });
      for (int color : colors) {
        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
        r.setColor(color);
        renderer.addSeriesRenderer(r);
      }
      return renderer;
    }
    
    
    
    
    public void startScore() {
        Log.i("wtf", "wtf!");
        Intent intent = new Intent(this, ScoreSheet.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//        overridePendingTransition(0,0);
    }
    
}
