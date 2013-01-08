package com.aceanuu.swss;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import org.achartengine.GraphicalView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aceanuu.swss.sqlite.DatabaseManager;
import com.aceanuu.swss.stats.PlayerStat;
import com.actionbarsherlock.app.SherlockFragment;
import com.viewpagerindicator.TabPageIndicator;


public class Stats extends SherlockFragment{

    private GraphicalView mChartView;
    boolean finished_starting = false;

    LinearLayout frag_root;
    TabPageIndicator tab_root;
    ViewPager      stat_pager;

	DatabaseManager dbm;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        dbm = new DatabaseManager(getActivity());
    	frag_root = (LinearLayout) inflater.inflate(R.layout.stats_ui, null);
    	tab_root  = (TabPageIndicator) frag_root.findViewById(R.id.stats_tabs);
    	stat_pager= (ViewPager) frag_root.findViewById(R.id.stats_pager);
    	StatSelectAdapter adapater = new StatSelectAdapter(getActivity());
    	stat_pager.setAdapter(adapater); 
    	tab_root.setViewPager(stat_pager);
    	
    	stat_pager.setOffscreenPageLimit(4);
    	
    	ArrayList<FrameLayout> tab_bgs = tab_root.getTabsArray();
    	
    	ArrayList<Integer> drawable_tabs = new ArrayList<Integer>();
    	drawable_tabs.add(R.drawable.player);
    	drawable_tabs.add(R.drawable.game);
    	drawable_tabs.add(R.drawable.category);
    	drawable_tabs.add(R.drawable.wonder);
    	
    	for(int i = 0; i < 4; ++i)
    	{
        	ImageView img = new ImageView(getActivity());
        	img.setImageDrawable(getActivity().getResources().getDrawable(drawable_tabs.get(i)));
        	img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        	tab_bgs.get(i).addView(img); 
        	FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(img.getLayoutParams());
        	layoutParams.setMargins(12, 12, 12, 12);
        	img.setLayoutParams(layoutParams);  
    	}
    	
        
//        stat_adpater     = new StatSelectAdapter();
//        stat_pager.setAdapter(stat_adpater);
//        tab_root.set
//        
//        
//        tab_root.add
//        Spinner pspinner = (Spinner) frag_root.findViewById(R.id.tab_player_select_player);
//        
//        
//        final DatabaseManager db = new DatabaseManager(getActivity());
//        
//        final HashMap<String, Integer> pidmap = db.getPlayerNamePIDs();        
//        final ArrayList<String>  names = new ArrayList<String>(pidmap.keySet());
//        ArrayAdapter<String> spinneradpater = new ArrayAdapter<String>(
//        										  getActivity().getApplicationContext(),
//                                                  R.layout.autocomplete,
//                                                  names);
//        pspinner.setAdapter(spinneradpater);
//        
//        final TextView info = (TextView) frag_root.findViewById(R.id.tab_player_info);
//        
//        pspinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1,
//                    int position, long arg3) {
//                String infos;
//                
//                int pid = pidmap.get(names.get(position));
//                Log.i("onItemSelected - Player", "pid: " + pid);
//                infos = db.getPlayerHistory(names.get(position), pid).toString();
//                info.setText(infos);
//                
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//                
//            }
//            
//        });
        
        return frag_root;
        
    }
    //
//  
//  
////  TabWidget tab = (TabWidget) findViewById(R.id.tabs);
//  TabHost tab_host = (TabHost) fragRoot.findViewById(android.R.id.tabhost);
//  TabHost.TabSpec ts;
//  tab_host.setup();
//  ts = tab_host.newTabSpec("Players")
//  		.setIndicator("", getResources().getDrawable(R.drawable.player))
//  		.setContent(R.id.player_tab);
//  tab_host.addTab(ts);
//  ts = tab_host.newTabSpec("Games")
//  		.setIndicator("", getResources().getDrawable(R.drawable.game))
//  		.setContent(R.id.chart2);
//  tab_host.addTab(ts);
//  ts = tab_host.newTabSpec("Categories")
//  		.setIndicator("", getResources().getDrawable(R.drawable.category))
//  		.setContent(R.id.chart);
//  tab_host.addTab(ts);
//  ts = tab_host.newTabSpec("Wonders")
//  		.setIndicator("", getResources().getDrawable(R.drawable.wonder))
//  		.setContent(R.id.chart);
//  tab_host.addTab(ts);
//  
//  tab_host.setCurrentTab(0);
//   
//  
//  
//  List<double[]> values = new ArrayList<double[]>();
//  values.add(new double[] { 12, 14, 11, 10, 19, 12, 14, 11, 10 });
//  
//  List<String[]> titles = new ArrayList<String[]>();
//  titles.add(new String[] { "Military", "Money", "Wonder", "Civilian", "Commercial", "Science", "Guild", "Leader", "Cities" });
//  int[] colors = new int[] { 
//  getResources().getColor(R.color.military)
//  ,getResources().getColor(R.color.money)
//  ,getResources().getColor(R.color.wonder)
//  ,getResources().getColor(R.color.civilian)
//  ,getResources().getColor(R.color.commercial)
//  ,getResources().getColor(R.color.science)
//  ,getResources().getColor(R.color.guild)
//  ,getResources().getColor(R.color.leader)
//  ,getResources().getColor(R.color.cities) };
  
//  DefaultRenderer renderer = buildCategoryRenderer(colors);
//  renderer.setLabelsColor(Color.BLACK);
//
//  LinearLayout layout = (LinearLayout) fragRoot.findViewById(R.id.chart);

//  mChartView = ChartFactory.getDoughnutChartView(getApplicationContext(), buildMultipleCategoryDataset("Project budget", titles, values), renderer);
//  
//  mChartView = ChartFactory.getDoughnutChartView(getActivity().getApplicationContext(),
//      buildMultipleCategoryDataset("Project budget", titles, values), renderer);
//  
//  layout.addView(mChartView, new LayoutParams(LayoutParams.WRAP_CONTENT,
//          LayoutParams.WRAP_CONTENT));
//
//   layout = (LinearLayout) fragRoot.findViewById(R.id.chart2);
//  mChartView = ChartFactory.getDoughnutChartView(getActivity().getApplicationContext(),
//      buildMultipleCategoryDataset("Project budget", titles, values), renderer);
//
//  layout.addView(mChartView, new LayoutParams(LayoutParams.WRAP_CONTENT,
//          LayoutParams.WRAP_CONTENT));
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
 
    public class StatSelectAdapter extends PagerAdapter{
    	
    	final static int num_stat_pages = 4;

		Context ctx; 
		PlayerSelectAdapter player;
		
    	public StatSelectAdapter(Context _ctx) {
    		ctx = _ctx;
    		player = new PlayerSelectAdapter(ctx);
		}
 
  

        @Override
        public LinearLayout instantiateItem(View collection, int position) {
//			   player	
//			
//		   Most Wins	Best Win/Loss
//		   Highest Scoring	Most Games Played

			LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout player_row = (LinearLayout) inflater.inflate(R.layout.stat_select_page, null);
			TextView top_left = (TextView) player_row.findViewById(R.id.top_row_left);
			TextView top_right = (TextView) player_row.findViewById(R.id.top_row_right);
			TextView top_left_label = (TextView) player_row.findViewById(R.id.top_row_left_label);
			TextView top_right_label = (TextView) player_row.findViewById(R.id.top_row_right_label);
			
			TextView bottom_left = (TextView) player_row.findViewById(R.id.bottom_row_left);
			TextView bottom_right = (TextView) player_row.findViewById(R.id.bottom_row_right);
			TextView bottom_left_label = (TextView) player_row.findViewById(R.id.bottom_row_left_label);
			TextView bottom_right_label = (TextView) player_row.findViewById(R.id.bottom_row_right_label);
			
//			TextView list_label = (TextView) player_row.findViewById(R.id.list_label);
			ListView select_list  = (ListView) player_row.findViewById(R.id.select_list);
			Log.w("making shit shit", "well fuck not yet");
//			if(position == 0)
//			{
				Log.w("making shit shit", "fuckin doin' shit!");
				PlayerStat most_wins   = dbm.getMostWinPlayer();
				PlayerStat high_score  = most_wins;// dbm.getHighScorePlayer();
				PlayerStat most_played = most_wins;//dbm.getMostPlayPlayer();
				PlayerStat best_wl     = most_wins;//dbm.getBestWL();
				
//				list_label.setText("All Players");
				select_list.setAdapter(player);
				
				top_right_label.setText("Best W/L Ratio");
				top_left_label.setText("Most Wins");
				bottom_right_label.setText("Most Games Played");
				bottom_left_label.setText("Highest Score");
				

				top_right.setText(best_wl.toLabelString());
				top_left.setText(most_wins.toLabelString());
				bottom_right.setText(most_played.toLabelString());
				bottom_left.setText(high_score.toLabelString());

	            ((ViewPager) collection).addView(player_row, 0);
				return player_row;
//			}
//			return player_row;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}



		@Override
		public int getCount() {
			return num_stat_pages;
		}

    }
    
   public class PlayerSelectAdapter extends BaseAdapter{
    	

       ArrayList<PlayerStat> player_list;
	   Context ctx; 
	   
    	public PlayerSelectAdapter(Context _ctx) {
    		ctx = _ctx;
    		player_list = dbm.getPlayerStatList();
    		Collections.sort(player_list, new PlayerDateComparator());
		}
    	
    	public void updatePlayerStats()
    	{
    		player_list = dbm.getPlayerStatList();
    		Collections.sort(player_list, new PlayerDateComparator());
    		this.notifyDataSetChanged();
    	}

		@Override
		public int getCount() {
			return player_list.size();
		}

		@Override
		public PlayerStat getItem(int position) {
			return player_list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View player_row = inflater.inflate(R.layout.stat_tworow, null);
		    TextView player_name = (TextView) player_row.findViewById(R.id.title);
		    TextView player_date = (TextView) player_row.findViewById(R.id.subtitle);
		    
		    Date date = new Date(player_list.get(position).datelong);
		    SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, ''yy  HH:mm a");
		    
		    player_name.setText(player_list.get(position).name);
		    player_date.setText("Last Played: " + format.format(date).toString());
	    	
			return player_row;
		}
    }
    
   public class PlayerDateComparator implements Comparator<PlayerStat>{
	   
	    @Override
	    public int compare(PlayerStat lhs, PlayerStat rhs) {
	        return (int) (rhs.datelong - lhs.datelong);
	    }
	}
}
