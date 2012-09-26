package com.aceanuu.swss;

import com.aceanuu.swss.logic.Stages;
import com.aceanuu.swss.logic.Wonders;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


public class ViewPagerListener implements ViewPager.OnPageChangeListener{

    CustomViewPager      pager;
    LinearLayout   createBar;
    LinearLayout   resultBar;
    Wonders[]      wonderList;
    Stages[]       stagesList;
    Context        ctx;
    
    public ViewPagerListener(Context _ctx, CustomViewPager _pager, LinearLayout edit_players_bar, LinearLayout results_bar){
        
        ctx        = _ctx;
        pager      = _pager;
        createBar  = results_bar;
        resultBar  = edit_players_bar;
        wonderList = Wonders.values();
        stagesList = Stages.values();
    }
    
    @Override
    public void onPageScrollStateChanged(int arg0) {
        
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        
    }

    @Override
    public void onPageSelected(int _selectedPage) {

        if (_selectedPage == 0) {
            pager.setChildId(0);
        } else {
            pager.setChildId(20);
        }
        
        if(stagesList[_selectedPage] == Stages.PLAYERS)
        {
            createBar.setVisibility(View.VISIBLE);
            resultBar.setVisibility(View.GONE);
        }else
        if(stagesList[_selectedPage] == Stages.RESULTS)
        {
            resultBar.setVisibility(View.VISIBLE);
            createBar.setVisibility(View.GONE);
        }else
        {
            createBar.setVisibility(View.GONE);
            resultBar.setVisibility(View.GONE);
        }
            
        
    }

}
