package com.aceanuu.swss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class SevenWondersScoreSheetMain extends SherlockFragmentActivity {
	
	FragmentManager fragmentManager;
	public final String SCORESHEET_TAG = "SS_FRAG";
	public final String STAT_TAG = "STAT_FRAG";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.i("onCreate main shit", "begin");
        setContentView(R.layout.fragmenthost);

		Log.i("onCreate main shit", "1");
		ActionBar ab = getSupportActionBar();
		ab.setDisplayShowTitleEnabled(false);
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		Log.i("onCreate main shit", "2");
		SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.action_bar_nav, android.R.layout.simple_spinner_dropdown_item);
		OnNavigationListener actionBarListNavListener = new OnNavigationListener() {
		    boolean firstRun = false;
			
			@Override
		    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		    	
				if(!firstRun)
				{
					firstRun = true;
					return false;
				}
				
		        if(itemPosition == 0) 
		        {
		        	return true;
		        }
		        	
		        if(itemPosition == 1) //if other thing
		        {
		        	return true;
		        }
		        
		        return false;
		    }
		};

		Log.i("onCreate main shit", "3");
		ab.setListNavigationCallbacks(mSpinnerAdapter, actionBarListNavListener);

		if(savedInstanceState == null) {
	        fragmentManager = getSupportFragmentManager();
			Log.i("onCreate main shit", "4");
	        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			Log.i("onCreate main shit", "5");
	        ScoreSheet ssfrag = new ScoreSheet();
			Log.i("onCreate main shit", "6");
	        ssfrag.setHasOptionsMenu(true);
			Log.i("onCreate main shit", "7");
	        fragmentTransaction.add(R.id.fragment_container, ssfrag, SCORESHEET_TAG);
			Log.i("onCreate main shit", "8");
	        fragmentTransaction.commit();
			Log.i("onCreate main shit", "9");
		 }
		Log.i("onCreate main shit", "10");
	
	        
	}
	

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	((ScoreSheet)fragmentManager.findFragmentByTag(SCORESHEET_TAG)).updateAfterSettings();
    }
}