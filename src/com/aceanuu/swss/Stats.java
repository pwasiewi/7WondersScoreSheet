package com.aceanuu.swss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.aceanuu.swss.sqlite.DatabaseManager;
import com.actionbarsherlock.app.SherlockFragment;


public class Stats extends SherlockFragment{

    private GraphicalView mChartView;
    boolean finished_starting = false;

    LinearLayout fragRoot;

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	fragRoot = (LinearLayout) inflater.inflate(R.layout.stats, null);
//
//        
//        
////        TabWidget tab = (TabWidget) findViewById(R.id.tabs);
//        TabHost tab_host = (TabHost) fragRoot.findViewById(android.R.id.tabhost);
//        TabHost.TabSpec ts;
//        tab_host.setup();
//        ts = tab_host.newTabSpec("Players")
//        		.setIndicator("", getResources().getDrawable(R.drawable.player))
//        		.setContent(R.id.player_tab);
//        tab_host.addTab(ts);
//        ts = tab_host.newTabSpec("Games")
//        		.setIndicator("", getResources().getDrawable(R.drawable.game))
//        		.setContent(R.id.chart2);
//        tab_host.addTab(ts);
//        ts = tab_host.newTabSpec("Categories")
//        		.setIndicator("", getResources().getDrawable(R.drawable.category))
//        		.setContent(R.id.chart);
//        tab_host.addTab(ts);
//        ts = tab_host.newTabSpec("Wonders")
//        		.setIndicator("", getResources().getDrawable(R.drawable.wonder))
//        		.setContent(R.id.chart);
//        tab_host.addTab(ts);
//        
//        tab_host.setCurrentTab(0);
//         
//        
//        
//        List<double[]> values = new ArrayList<double[]>();
//        values.add(new double[] { 12, 14, 11, 10, 19, 12, 14, 11, 10 });
//        
//        List<String[]> titles = new ArrayList<String[]>();
//        titles.add(new String[] { "Military", "Money", "Wonder", "Civilian", "Commercial", "Science", "Guild", "Leader", "Cities" });
//        int[] colors = new int[] { 
//        getResources().getColor(R.color.military)
//        ,getResources().getColor(R.color.money)
//        ,getResources().getColor(R.color.wonder)
//        ,getResources().getColor(R.color.civilian)
//        ,getResources().getColor(R.color.commercial)
//        ,getResources().getColor(R.color.science)
//        ,getResources().getColor(R.color.guild)
//        ,getResources().getColor(R.color.leader)
//        ,getResources().getColor(R.color.cities) };
        
//        DefaultRenderer renderer = buildCategoryRenderer(colors);
//        renderer.setLabelsColor(Color.BLACK);
//
//        LinearLayout layout = (LinearLayout) fragRoot.findViewById(R.id.chart);

//        mChartView = ChartFactory.getDoughnutChartView(getApplicationContext(), buildMultipleCategoryDataset("Project budget", titles, values), renderer);
//        
//        mChartView = ChartFactory.getDoughnutChartView(getActivity().getApplicationContext(),
//            buildMultipleCategoryDataset("Project budget", titles, values), renderer);
//        
//        layout.addView(mChartView, new LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT));
//
//         layout = (LinearLayout) fragRoot.findViewById(R.id.chart2);
//        mChartView = ChartFactory.getDoughnutChartView(getActivity().getApplicationContext(),
//            buildMultipleCategoryDataset("Project budget", titles, values), renderer);
//
//        layout.addView(mChartView, new LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT));
        
        Spinner pspinner = (Spinner) fragRoot.findViewById(R.id.tab_player_select_player);
        
        
        final DatabaseManager db = new DatabaseManager(getActivity());
        
        final HashMap<String, Integer> pidmap = db.getPlayerNamePIDs();        
        final ArrayList<String>  names = new ArrayList<String>(pidmap.keySet());
        ArrayAdapter<String> spinneradpater = new ArrayAdapter<String>(
        										  getActivity().getApplicationContext(),
                                                  R.layout.autocomplete,
                                                  names);
        pspinner.setAdapter(spinneradpater);
        
        final TextView info = (TextView) fragRoot.findViewById(R.id.tab_player_info);
        
        pspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int position, long arg3) {
                String infos;
                
                int pid = pidmap.get(names.get(position));
                Log.i("onItemSelected - Player", "pid: " + pid);
                infos = db.getPlayerHistory(names.get(position), pid).toString();
                info.setText(infos);
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                
            }
            
        });
        
        return fragRoot;
        
    }
//
//    /**
//     * Builds a multiple category series using the provided values.
//     * 
//     * @param titles the series titles
//     * @param values the values
//     * @return the category series
//     */
//    protected MultipleCategorySeries buildMultipleCategoryDataset(String title,
//        List<String[]> titles, List<double[]> values) {
//      MultipleCategorySeries series = new MultipleCategorySeries(title);
//      int k = 0;
//      for (double[] value : values) {
//        series.add(2007 + k + "", titles.get(k), value);
//        k++;
//      }
//      return series;
//    }
//    
//
//    /**
//     * Builds a category renderer to use the provided colors.
//     * 
//     * @param colors the colors
//     * @return the category renderer
//     */
//    protected DefaultRenderer buildCategoryRenderer(int[] colors) {
//      DefaultRenderer renderer = new DefaultRenderer();
//      renderer.setLabelsTextSize(25);
//      renderer.setShowLegend(false);
//      renderer.setZoomEnabled(false);
//      renderer.setExternalZoomEnabled(false);
//      renderer.setPanEnabled(false);
//      renderer.setMargins(new int[] { 20, 30, 15, 0 });
//      for (int color : colors) {
//        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
//        r.setColor(color);
//        renderer.addSeriesRenderer(r);
//      }
//      return renderer;
//    }
    
}
