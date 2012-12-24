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
	private ScoreSheet ss_frag;
	private Stats      stats_frag;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmenthost);

		ActionBar ab = getSupportActionBar();
		ab.setDisplayShowTitleEnabled(false);
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

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
				    // Create new fragment from our own Fragment class
			        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				    // Replace whatever is in the fragment container with this fragment
				    //  and give the fragment a tag name equal to the string at the position selected
			        fragmentTransaction.replace(R.id.fragment_container, ss_frag, SCORESHEET_TAG);
				    // Apply changes
			        fragmentTransaction.commit();
				    return true;
		        }
		        	
		        if(itemPosition == 1) //if other thing
		        {
				    // Create new fragment from our own Fragment class
			        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				    // Replace whatever is in the fragment container with this fragment
				    //  and give the fragment a tag name equal to the string at the position selected
			        fragmentTransaction.replace(R.id.fragment_container, stats_frag, STAT_TAG);
				    // Apply changes
			        fragmentTransaction.commit();
				    return true;
		        }
		        
		        return false;
		    }
		};

		ab.setListNavigationCallbacks(mSpinnerAdapter, actionBarListNavListener);

        ss_frag = new ScoreSheet();
        ss_frag.setHasOptionsMenu(true);
        stats_frag = new Stats();
        stats_frag.setHasOptionsMenu(true);
        
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, ss_frag, SCORESHEET_TAG);
        fragmentTransaction.commit();	        
	}
	

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("main shit", "onActivityResult");
    	((ScoreSheet)fragmentManager.findFragmentByTag(SCORESHEET_TAG)).updateAfterSettings();
    }
}