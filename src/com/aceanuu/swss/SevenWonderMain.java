package com.aceanuu.swss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aceanuu.swss.logic.Game;
import com.aceanuu.swss.logic.Stages;
import com.aceanuu.swss.logic.Wonders;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.TabPageIndicator;


public class SevenWonderMain extends SherlockActivity {
    
    private final int                    NUM_TABS;
    private int                          dp28;
    
    private Game                         current_game;
    
    private SevenWonderAdapter           pagerAdapter;
    private CustomViewPager              viewPager;
    
    private LayoutInflater               factory;
    private Context                      ctx;

    private final Wonders[]              wonder_list;
    private final Stages[]               stages_list;
    private String[]                     tab_titles;
    
    private PlayerAdapter                nameAdapter;
    private SumAdapter                   sumAdapter;
    private ArrayList<ScoreAdapter>      scoreAdapterList;
    private Map<Stages, Drawable>        stage_medals;
    
    
    public SevenWonderMain() {
        wonder_list   = Wonders.values();
        stages_list   = Stages.values();
        NUM_TABS      = stages_list.length;
    }
    
    
    public void initializeData() {
        current_game = new Game();
        tab_titles   = getResources().getStringArray(R.array.tabs);

        nameAdapter  = new PlayerAdapter();
        sumAdapter   = new SumAdapter();

        ctx          = this.getApplicationContext();
        
        final float scale = getResources().getDisplayMetrics().density;
        dp28 = (int) (28 * scale + 0.5f);
        
        scoreAdapterList = new ArrayList<ScoreAdapter>();
        
        for (Stages this_step : current_game.scoring_stages) 
            scoreAdapterList.add(new ScoreAdapter(this_step));
        
        TypedArray medal_images = getResources().obtainTypedArray(R.array.medals);
        stage_medals = new HashMap<Stages, Drawable>();  
        for(int i = 0; i < medal_images.length(); ++i) 
            stage_medals.put(current_game.scoring_stages.get(i), medal_images.getDrawable(i));
    }
    
    
    public void initializeViews() {
        getSupportActionBar().setTitle("Score Sheet");
        
        factory          = LayoutInflater.from(this);
        viewPager        = (CustomViewPager) findViewById(R.id.awesomepager);

        pagerAdapter     = new SevenWonderAdapter();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setChildId(0);
    }
    
    
    public void initializeListeners() {
        final LinearLayout players_bar      = (LinearLayout)   this.findViewById(R.id.create_bottom_bar);
        final LinearLayout results_bar      = (LinearLayout)   this.findViewById(R.id.results_bottom_bar);
        final LinearLayout bottom_container = (LinearLayout)   this.findViewById(R.id.bottom_bar);

        final LinearLayout players_edit_click    = (LinearLayout) this.findViewById(R.id.edit_wonder_bottom);
        final LinearLayout players_remove_click  = (LinearLayout) this.findViewById(R.id.remove_player_bottom);

        players_remove_click.setEnabled(true);
        players_edit_click.setEnabled(true);
        
        final TabPageIndicator tabInd = (TabPageIndicator) findViewById(R.id.indicatorzulu);
        tabInd.setViewPager(viewPager);

        tabInd.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {}

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}

            @Override
            public void onPageSelected(int position) {
                Stages this_stage = stages_list[position];
                
                if (this_stage == Stages.RESULTS) {
                    current_game.generateResults();
                    viewPager.setChildId(20);
                    
                    bottom_container.setVisibility(View.VISIBLE);
                    players_bar.setVisibility(View.GONE);
                    results_bar.setVisibility(View.VISIBLE);
                    
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(tabInd.getWindowToken(), 0);
                }
                else if (this_stage == Stages.PLAYERS) 
                {
                    viewPager.setChildId(0);
                    bottom_container.setVisibility(View.VISIBLE);
                    players_bar.setVisibility(View.VISIBLE);
                    results_bar.setVisibility(View.GONE);
                }
                else
                {
                    bottom_container.setVisibility(View.GONE);
                }
            }
        });
        

        players_edit_click.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!nameAdapter.editMode)
                {
                    nameAdapter.toggleEditMode();
                    Button edit_button = (Button) findViewById(R.id.edit_wonder_button);
                    edit_button.setText(R.string.change_selected_prompt);
                    players_remove_click.setEnabled(false);
                }
                else
                {
                    nameAdapter.commitSelectedWonders();
                    nameAdapter.toggleEditMode();
                    Button edit_button = (Button) findViewById(R.id.edit_wonder_button);
                    edit_button.setText(R.string.change_wonder_prompt);
                    players_remove_click.setEnabled(true);
                }
            }
        });  
        
        players_remove_click.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!nameAdapter.removeMode)
                {
                    nameAdapter.toggleRemoveMode();
                    
                    Button remove_button = (Button) findViewById(R.id.remove_player_button);
                    remove_button.setText(R.string.remove_selected_prompt);
                    players_edit_click.setEnabled(false);
                }
                else
                {
                    nameAdapter.removeSelected();
                    nameAdapter.toggleRemoveMode();
                    
                    Button remove_button = (Button) findViewById(R.id.remove_player_button);
                    remove_button.setText(R.string.remove_players_prompt);
                    players_edit_click.setEnabled(true);
                }
            }
        });    
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ui);
        initializeData();
        initializeViews();
        initializeListeners();
    }
    

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Reset Scores").setNumericShortcut('0')
                .setIcon(R.drawable.ic_refresh)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add("Add Player").setNumericShortcut('3')
                .setIcon(R.drawable.ic_add)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        char sc = item.getNumericShortcut();
        if (sc == '0') {
            Dialog temp = new AlertDialog.Builder(this)
                    .setTitle("Reset Scores?")
                    .setPositiveButton("Reset",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    resetScores();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int whichButton) {
                                }
                            }).create();

            temp.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            temp.show();
            return true;
        } else 
        if (sc == '3') 
        {
            addPlayerPrompt();
            return true;
        }
        return false;
    }

    
    public void resetScores() {
        current_game.newGame();
        viewPager.setCurrentItem(0);
        notifyAllAdapters();
    }

    
    private void addPlayerPrompt() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final View playerPromptView = factory.inflate(R.layout.player_dialog, null);
        final Spinner spinnerWonder = (Spinner) playerPromptView.findViewById(R.id.wonder_spinner);
        final EditText inputName = (EditText) playerPromptView.findViewById(R.id.player_name);
        
        inputName.requestFocus();
        alert.setTitle("Player Name");
        alert.setView(playerPromptView);
        
        spinnerWonder.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputName.getWindowToken(), 0);
                return false;
            }
        });
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                if (inputName.getText().toString().length() > 0)
                {
                    addPlayer(inputName.getText().toString(),  wonder_list[spinnerWonder.getSelectedItemPosition()]);
                }
                else 
                {
                    Toast.makeText(ctx, "Must Enter a Player Name",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertToShow = alert.create();

        alertToShow.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertToShow.show();
    }

    
    private void addPlayer(String name, Wonders wonder) {
        current_game.addPlayer(name, wonder);
        notifyAllAdapters();
    }

    
    private void removePlayer(int position) {
        current_game.removePlayer(position);
        notifyAllAdapters();
    }

    
    public void notifyAllAdapters() {
        nameAdapter.notifyDataSetChanged();
        sumAdapter.notifyDataSetChanged();
        for (ScoreAdapter temp : scoreAdapterList) {
            temp.notifyDataSetChanged();
        }
    }

    
    public class SevenWonderAdapter extends PagerAdapter {
     
        @Override
        public int getCount() {
            return NUM_TABS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tab_titles[position % tab_titles.length].toUpperCase();
        }

        @Override
        public Object instantiateItem(View collection, int position) {

            Stages this_stage = stages_list[position];
            
            View temp = getLayoutInflater().inflate(R.layout.tabpanel, null);
            ((ViewPager) collection).addView(temp, 0);

            if (this_stage == Stages.PLAYERS) 
            {
                ListView content = (ListView) temp.findViewById(R.id.pagerContent);
                content.setAdapter(nameAdapter);

                return temp;
            } 
            else if (this_stage == Stages.RESULTS) 
            {
                ListView content = (ListView) temp.findViewById(R.id.pagerContent);
                content.setAdapter(sumAdapter);
                return temp;
            }
            else 
            {
                ListView content = (ListView) temp.findViewById(R.id.pagerContent);
                content.setAdapter(scoreAdapterList.get(position - 1));
                View tempView = content.getChildAt(0);
                if(tempView != null)
                {
                    tempView = tempView.findViewById(R.id.stepScore);
                    if(tempView != null)
                    {
                        tempView.requestFocus();
                    }
                }
                return temp;
            }
        }

        @Override
        public void destroyItem(View collection, int position, Object view) {
            ((ViewPager) collection).removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public void finishUpdate(View arg0) {}

        @Override
        public void startUpdate(View arg0) {}

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {}

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

    
    private class PlayerAdapter extends BaseAdapter {

        boolean removeMode;
        boolean editMode;
        ArrayList<CheckBox> removeList;
        ArrayList<Spinner>  spinnerList;
        
        public PlayerAdapter() {
            removeMode = false;
            removeList = new ArrayList<CheckBox>();
            spinnerList= new ArrayList<Spinner>();
        }

        public void commitSelectedWonders() {
            for(int i = 0; i < current_game.playerCount(); ++i)
            {
                current_game.getPlayer(i).setWonder(wonder_list[spinnerList.get(i).getSelectedItemPosition()]);
            }
        }

        public void toggleEditMode() {
            if(!editMode)
            {
                spinnerList= new ArrayList<Spinner>();
            }
            editMode = !editMode;
            this.notifyDataSetChanged();
        }

        public void toggleRemoveMode() {
            if(!removeMode)
            {
                removeList = new ArrayList<CheckBox>();
            }
            removeMode = !removeMode;
            this.notifyDataSetChanged();
            
        }
        
        public void removeSelected() {
            ArrayList<Integer> positionsToRemove = new ArrayList<Integer>();
            for(CheckBox thisBox : removeList)
            {
                if(thisBox.isChecked())
                {
                    positionsToRemove.add((Integer)thisBox.getTag());
                }
            }
            
            for(int i = 0; i < positionsToRemove.size(); ++i)
            {
                removePlayer(positionsToRemove.get(i)-i);
            }
        }

        @Override
        public int getCount() {
            return current_game.playerCount();
        }
        
        @Override
        public Object getItem(int position) {
            return current_game.getPlayer(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RelativeLayout tempView;
            if(removeMode)
            {
                tempView = (RelativeLayout) LayoutInflater.from(ctx).inflate(R.layout.player_item_remove, null);
                
                TextView wonderName = (TextView) tempView.findViewById(R.id.wonderName);
                wonderName.setText(current_game.getPlayer(position).getWonderName());
                
                CheckBox remove = (CheckBox) tempView.findViewById(R.id.removePlayerCheck);
                remove.setTag(position);
                removeList.add(remove);
            }else if(editMode)
            {
                tempView = (RelativeLayout) LayoutInflater.from(ctx).inflate(R.layout.player_item_edit, null);

                Spinner wonderSpinner = (Spinner) tempView.findViewById(R.id.wonderName);
                wonderSpinner.setSelection(current_game.getPlayer(position).getWonder().ordinal());
                spinnerList.add(wonderSpinner);
                
            }else
            {
                tempView = (RelativeLayout) LayoutInflater.from(ctx).inflate(R.layout.player_item, null);
                
                TextView wonderName = (TextView) tempView.findViewById(R.id.wonderName);
                wonderName.setText(current_game.getPlayer(position).getWonderName());
            }

            TextView playerName = (TextView) tempView.findViewById(R.id.playerName);
            playerName.setText(current_game.getPlayer(position).getName());
            
            return tempView;
        }
    }
    

    private class ScoreAdapter extends BaseAdapter {

        Stages                                    stage;
        private LayoutInflater                mInflater;

        public ScoreAdapter(Stages _stage) {
            stage             = _stage;
            mInflater         = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return current_game.playerCount();
        }

        @Override
        public Object getItem(int position) {
            return current_game.getPlayerStageScore(position, stage);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                Log.d("getV", "convertView is null, pos " + position);
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.score_item, null);

                holder.val = (EditText) convertView.findViewById(R.id.stepScore);                
                
                holder.name = (TextView) convertView.findViewById(R.id.playerName);
                holder.name.setText(current_game.getPlayer(position).getName()); 
               
                holder.pos = (Button) convertView.findViewById(R.id.pos);
                holder.neg = (Button) convertView.findViewById(R.id.neg);
                
                convertView.setTag(holder);
            } else {
                Log.w("getV", "convertView is not null, pos " + position);
                holder = (ViewHolder) convertView.getTag();
            }
                        
            final int f_position = position;
            
            //Fill EditText with the value you have in data source
            int score_item = current_game.getPlayerStageScore(position, stage);
            if(score_item != 0)
                holder.val.setText(score_item + "");
                
            holder.val.setId(position);

            //so it can be used below
            final EditText score = holder.val;
            
            holder.pos.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try {
                          int scoreval = Integer.parseInt(score.getText().toString());
                          scoreval++;
                          score.setText(scoreval + "");
                    }catch(Exception e)
                    {
                        score.setText("1");
                    }
                }
            });
            
            holder.neg.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try {
                        int scoreval = Integer.parseInt(score.getText().toString());
                        scoreval--;
                        score.setText(scoreval + "");
                    }
                    catch(Exception e)
                    {
                        score.setText("-1");  
                    }
                }
                
            });
         
            holder.val.addTextChangedListener(new TextWatcher() {
                
                public void afterTextChanged(Editable s) {
                    
                    int value;
                    if (score.getText().toString().equals("") || score.getText().toString().equals("-"))
                    {
                        value = 0;
                    }else
                    if (score.getText().toString().equals("0") )
                    {
                        value = 0;
                        score.setText("");
                    }
                    else 
                    {  
                        try 
                        {
                            value = Integer.parseInt(score.getText().toString());
                        }
                        catch(Exception e)
                        {
                            score.setText("");
                            value = 0;
                        }
                    }
                    current_game.setPlayerStageScore(f_position, stage, value);
                }      
                
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
            
            return convertView;
        }
    }

    
    class ViewHolder {
        TextView name;
        EditText val;
        Button pos;
        Button neg;
    }

    
    private class SumAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return current_game.playerCount();
        }

        @Override
        public Object getItem(int position) {
            return current_game.getPlayer(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RelativeLayout tempView = (RelativeLayout) LayoutInflater.from(ctx).inflate(R.layout.result_item, null);
            TextView name_box       = (TextView) tempView.findViewById(R.id.playerName);
            TextView sum_box        = (TextView) tempView.findViewById(R.id.totalScore);
            TextView position_box   = (TextView) tempView.findViewById(R.id.finishingPosition);
            LinearLayout badge_box  = (LinearLayout) tempView.findViewById(R.id.medalBox);
            
            name_box.setText(current_game.getPlayer(position).getName());
            sum_box.setText(current_game.getPlayer(position).getTotal() + "");
            position_box.setText(current_game.getPlayer(position).getPlace() + "");
            badge_box.setVisibility(View.VISIBLE);

            for (Stages step_won : current_game.getPlayer(position).getStageWins()) {
                if(stage_medals.containsKey(step_won))
                {
                    ImageView tempIV     = new ImageView(ctx);
                    Bitmap bmp           =  ((BitmapDrawable)stage_medals.get(step_won)).getBitmap();
                    Bitmap resizedbitmap = Bitmap.createScaledBitmap(bmp, dp28, dp28, true);
                    tempIV.setImageBitmap(resizedbitmap);
                    badge_box.addView(tempIV);
                }
            }

            if (current_game.getPlayer(position).getPlace() == 1) {
                sum_box.setTextColor(Color.GREEN);
                position_box.setTextColor(Color.GREEN);
            }

            return tempView;
        }
    }
}