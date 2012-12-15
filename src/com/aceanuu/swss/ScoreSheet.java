package com.aceanuu.swss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aceanuu.swss.logic.Game;
import com.aceanuu.swss.logic.STAGE;
import com.aceanuu.swss.logic.WONDER;
import com.aceanuu.swss.sqlite.DatabaseManager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.TabPageIndicator;

public class ScoreSheet extends SherlockActivity {
    
    private int                          NUM_TABS;
    private int                          dp28;
    
    private Game                         current_game;
    
    private SevenWonderAdapter           pagerAdapter;
    private CustomViewPager              viewPager;

    private LinearLayout                 players_wonder_click;
    private LinearLayout                 players_remove_click;
    private LinearLayout                 results_save_ll;
    private LinearLayout                 results_sort_click;
    
    private LayoutInflater               factory;
    private Context                      ctx;

    private WONDER[]                     wonder_list;
    private String[]                     tab_titles;
    
    private PlayerAdapter                nameAdapter;
    private SumAdapter                   sumAdapter;
    private ScienceAdapter               science_adapter;
    
    private Map<STAGE, Drawable>         stage_medals;
    HashMap<STAGE, Integer>              colors;
    private Map<STAGE, ScoreAdapter>     score_adapter_map;

    private  ArrayList<STAGE>            stages;
    
    public final static int TABLET       = 0;
    public final static int COG          = 1;
    public final static int COMPASS      = 2;
    public final static int WILD         = 3;
    
    private final static String PREF_KEY              = "seven_wonders_score_sheet_preferences";
    private final static String PREFS_PREV_PLAYER_KEY = "seven_wonders_prev_player_key";
    private final static String PREFS_PREV_DIVIDER    = "#@4%@A%@0";
    private final static String PREFS_PREV_NOTHING    = "NO_PLAYERS";
    
    TabPageIndicator tabInd;
    String science_key;
    String leaders_key;
    String cities_key;
    
    SharedPreferences prefs;
    DatabaseManager   dbm;
    
    ArrayList<String> player_names;
    boolean addedNewPlayerSinceLastNameCache; 
    
    
    /**
     * Called on app creation.
     * Configures and initializes app's necessary data members
     */
    public void initializeData() {
        current_game = new Game();
        tab_titles   = getResources().getStringArray(R.array.tabs); 
        prefs        = getSharedPreferences(PREF_KEY, MODE_PRIVATE); 

        science_key = getApplicationContext().getResources().getString(R.string.settings_expanded_science_key);
        leaders_key = getApplicationContext().getResources().getString(R.string.settings_expansions_leaders_key);
        cities_key  = getApplicationContext().getResources().getString(R.string.settings_expansions_cities_key);
        
        current_game.expanded_science = prefs.getBoolean(science_key, true);
        current_game.leaders_enabled  = prefs.getBoolean(leaders_key, true);
        current_game.cities_enabled   = prefs.getBoolean(cities_key,  true);
        
        colors = new HashMap<STAGE, Integer>();
        colors.put(STAGE.PLAYERS, getResources().getColor(R.color.player));
        colors.put(STAGE.MILITARY,getResources().getColor(R.color.military));
        colors.put(STAGE.MONEY,getResources().getColor(R.color.money));
        colors.put(STAGE.DEBT,getResources().getColor(R.color.money));
        colors.put(STAGE.WONDER,getResources().getColor(R.color.wonder));
        colors.put(STAGE.CIVILIAN,getResources().getColor(R.color.civilian));
        colors.put(STAGE.COMMERCIAL,getResources().getColor(R.color.commercial));
        colors.put(STAGE.SCIENCE,getResources().getColor(R.color.science));
        colors.put(STAGE.GUILD,getResources().getColor(R.color.guild));
        colors.put(STAGE.LEADERS,getResources().getColor(R.color.leader));
        colors.put(STAGE.CITIES,getResources().getColor(R.color.cities)); 
        colors.put(STAGE.RESULTS,getResources().getColor(R.color.results));
        
        nameAdapter  = new PlayerAdapter();
        sumAdapter   = new SumAdapter();

        ctx          = this.getApplicationContext();

        
        final float scale = getResources().getDisplayMetrics().density;
        dp28 = (int) (28 * scale + 0.5f);
        
        score_adapter_map       = new HashMap<STAGE,ScoreAdapter>();
        
        for (STAGE this_step : current_game.scoring_stages) 
            score_adapter_map.put(this_step, new ScoreAdapter(this_step));
        

        wonder_list   = WONDER.values();
        
        buildStages();
        dbm = new DatabaseManager(this);
        
        //STUFF FOR EXPANDED SCIENCE
//        if(expanded_science)
//        {
            science_adapter   = new ScienceAdapter();
//        }
        
        TypedArray medal_images = getResources().obtainTypedArray(R.array.medals);
        stage_medals            = new HashMap<STAGE, Drawable>();  
        
        for(int i = 0; i < medal_images.length(); ++i) 
            stage_medals.put(current_game.scoring_stages.get(i), medal_images.getDrawable(i));
    }
    

    private void buildTabColors() {

        ArrayList<FrameLayout> tabs = tabInd.getTabsArray();
        Log.e("buildTabColors", "changed expansion packs + tabs " + tabs.size());
        for(int i = 0; i < NUM_TABS; ++i)
        {
            FrameLayout t = tabs.get(i);
            STAGE stage   = stages.get(i);
            int color     = colors.get(stage);
            t.setBackgroundColor(color);
            Log.e("buildTabColors", "changed expansion packs iteration  " + i) ;
        }
    }
    
    private void buildStages() {
        stages = new ArrayList<STAGE>();
        stages.add(STAGE.PLAYERS);
        stages.add(STAGE.MILITARY);
        stages.add(STAGE.MONEY);
        stages.add(STAGE.WONDER);
        stages.add(STAGE.CIVILIAN);
        stages.add(STAGE.COMMERCIAL);
        stages.add(STAGE.SCIENCE);
        stages.add(STAGE.GUILD);
        stages.add(STAGE.RESULTS);

        if(current_game.leaders_enabled)
        {
            int resultsindex = stages.indexOf(STAGE.RESULTS);
            stages.add(resultsindex, STAGE.LEADERS);
        }
        else
        {
            current_game.clearScoresCategory(STAGE.LEADERS);
        }
        
        if(current_game.cities_enabled)
        {
            int moneyindex = stages.indexOf(STAGE.MONEY);
            stages.add(moneyindex+1, STAGE.DEBT);
            int resultsindex = stages.indexOf(STAGE.RESULTS);
            stages.add(resultsindex, STAGE.CITIES);
        }
        else
        {
            current_game.clearScoresCategory(STAGE.DEBT);
            current_game.clearScoresCategory(STAGE.CITIES);
        }
        
        NUM_TABS = stages.size();
        
        
    }


    /**
     * Called on app creation.
     * Configures and initializes app's necessary UI components
     */
    public void initializeViews() {
        final ActionBar ab = getSupportActionBar();
        //ab.setTitle("Score Sheet");

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
                int x = 0;
                if(itemPosition == 0)  //if score sheet
                    x = 0;           //do nothing
                else if(itemPosition == 1) //if other thing
                {
                    startStats();
                    //ab.setSelectedNavigationItem(0);
                }
                
                
                return false;
            }

        });
        
        factory          = LayoutInflater.from(this);
        viewPager        = (CustomViewPager) findViewById(R.id.awesomepager);

        pagerAdapter     = new SevenWonderAdapter();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setChildId(0);
    }


    private void startStats() {
        Intent intent = new Intent(this, Stats.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//        overridePendingTransition(0,0);
    }
    
    /**
     * Called on app creation.
     * Configures and initializes app's necessary UI listeners and hooks
     */    
    public void initializeListeners() {
        final LinearLayout players_bar      = (LinearLayout)   this.findViewById(R.id.create_bottom_bar);
        final LinearLayout results_bar      = (LinearLayout)   this.findViewById(R.id.results_bottom_bar);
        final LinearLayout bottom_container = (LinearLayout)   this.findViewById(R.id.bottom_bar);

        players_wonder_click     = (LinearLayout) players_bar.findViewById(R.id.players_wonder_ll);
        players_remove_click   = (LinearLayout) players_bar.findViewById(R.id.players_remove_ll);
        
        results_save_ll  = (LinearLayout) results_bar.findViewById(R.id.results_save_ll);
//        results_savegame_click = (LinearLayout) results_bar.findViewById(R.id.results_save_ll);
        results_sort_click = (LinearLayout) results_bar.findViewById(R.id.results_sort_ll);
        
        tabInd = (TabPageIndicator) findViewById(R.id.indicatorzulu);
        tabInd.setViewPager(viewPager);
        
        tabInd.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {}

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}

            @Override
            public void onPageSelected(int position) {
                STAGE this_stage = stages.get(position);
                
                if (this_stage == STAGE.RESULTS) {
                    current_game.generateResults();
                    viewPager.setChildId(20);
                    
                    bottom_container.setVisibility(View.VISIBLE);
                    players_bar.setVisibility(View.GONE);
                    results_bar.setVisibility(View.VISIBLE);
                    
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(tabInd.getWindowToken(), 0);
                }
                else if (this_stage == STAGE.PLAYERS) 
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

        buildTabColors();
        
        players_wonder_click.setOnClickListener(new OnClickListener() {

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
                    players_wonder_click.setEnabled(false);
                }
                else
                {
                    nameAdapter.removeSelected();
                    nameAdapter.toggleRemoveMode();
                    
                    Button remove_button = (Button) findViewById(R.id.remove_player_button);
                    remove_button.setText(R.string.remove_players_prompt);
                    players_wonder_click.setEnabled(true);
                }
            }
        });    
        
        results_save_ll.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //showResetScores();
                showSaveScore();
            }

        });   
        
        results_sort_click.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                current_game.sortByScores();
                notifyAllAdapters();
            }
        });   
    }
    
    
    /**
     * Called on app creation.
     * Calls all necessary initializer methods to get app into starting state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ui);
        initializeData();
        initializeViews();
        initializeListeners();
        loadPrevPlayers();
        

    }
    

    /**
     * Called on app creation.
     * Create menu options for primary actionbar
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }


    /**
     * Called on menu item selection
     * Create dialogs for menu item selections
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
            case R.id.ab_newgame_item:
                if(current_game.changedSinceLastSave())
                    resultsNotSavedPrompt();
                else
                    showResetScores();
                return true;
            case R.id.ab_addplayer_item:
                addPlayerPrompt();
                return true;
            case R.id.ab_settings_item:
                startActivityForResult(new Intent(this, Settings.class), 0); 
                return true;
        }
        return false;
    }
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 

        getSupportActionBar().setSelectedNavigationItem(0);
        
        boolean previous_science = current_game.expanded_science;
        boolean previous_leader  = current_game.leaders_enabled;
        boolean previous_cities  = current_game.cities_enabled;

        current_game.expanded_science = prefs.getBoolean(science_key, current_game.expanded_science);
        current_game.leaders_enabled  = prefs.getBoolean(leaders_key, current_game.leaders_enabled);
        current_game.cities_enabled   = prefs.getBoolean(cities_key,  current_game.cities_enabled);

        int   current_page  = viewPager.getCurrentItem();
        STAGE current_stage = stages.get(current_page);
        
        if(current_game.cities_enabled != previous_cities || current_game.leaders_enabled != previous_leader)
        {
            viewPager.setAdapter(pagerAdapter);
            pagerAdapter.notifyDataSetChanged();
            buildStages(); 
            notifyAllAdapters();
            tabInd.notifyDataSetChanged();
            notifyAllAdapters();
            buildTabColors();
        }
        
        if(current_game.expanded_science != previous_science)
        {
            current_game.updatePlayerScienceScoring(current_game.expanded_science);
            current_game.computePlayersScienceScores(); 
            notifyAllAdapters();
            pagerAdapter.notifyDataSetChanged();
            pagerAdapter.instantiateItem(viewPager, stages.indexOf(STAGE.SCIENCE));
            viewPager.setAdapter(pagerAdapter);
        } 
        
        int index = stages.indexOf(current_stage);
        
        if(index == -1)
        {
            if(current_stage == STAGE.DEBT)
                --current_page;
            
            if(current_stage == STAGE.LEADERS)
                --current_page;
            
            if(current_stage == STAGE.CITIES)
            {
                --current_page;
                --current_page;
            }
            
            if(current_stage == STAGE.CITIES && !current_game.leaders_enabled)
                --current_page;
            
            index = current_page;
        }
        viewPager.setCurrentItem(index);
    }


    private void showResetScores() {  
        Dialog temp = new AlertDialog.Builder(this)
        .setTitle("Score a new game?")
        .setIcon(this.getResources().getDrawable(R.drawable.navigation_refresh))
        .setPositiveButton("New Game",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        beginNewGame();
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
    }

    private void showSaveScore() {
        Dialog temp = new AlertDialog.Builder(this)
        .setTitle("Save Scores?")
        .setIcon(this.getResources().getDrawable(R.drawable.result_save))
        .setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { 
                        current_game.saveGame(dbm);
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
    }

    
 
    
    /**
     * Called when user wants to start scoring a new game
     * Creates a new game and moves the user view to step 1
     */
    public void beginNewGame() {
//        finishPlayerEdits(); 
        current_game.newGame();
        viewPager.setCurrentItem(0);
        notifyAllAdapters();
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
        pagerAdapter.instantiateItem(viewPager, stages.indexOf(STAGE.SCIENCE));
    }


    /**
     * Called when user wants to cease modifying player configuration
     * Exit edit states on user properties
     */
//    private void finishPlayerEdits() {
//        if(nameAdapter.editMode)
//        {
//            nameAdapter.commitSelectedWonders();
//            nameAdapter.toggleEditMode();
//        }
//        else if(nameAdapter.removeMode)
//        {
//            nameAdapter.removeSelected();
//            nameAdapter.toggleRemoveMode();
//        } 
//    }


    /**
     * Called when user wants to create or otherwise destroy game without having saved results
     * Prompts the user to either save or continue without save
     */
    private void resultsNotSavedPrompt() {
        Dialog temp = new AlertDialog.Builder(this)
        .setTitle("Results not Saved")
        .setIcon(this.getResources().getDrawable(R.drawable.alert_stop))
        .setMessage("Results for this game have not been saved.")
        .setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        current_game.saveGame(dbm); 
                        showResetScores();
                    }
                })
        .setNegativeButton("Don't Save",
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
    }

    @Override
    public void onPause()
    { 

        Log.w("onPause", "BEGIN onPause ----------------------------------"); 
        super.onPause();
        SharedPreferences.Editor prefs_ed = prefs.edit(); 
        StringBuffer player_name_buffer   = new StringBuffer();
        
        if(current_game.playerCount() == 0)
        {

            Log.w("onPause", "no players, 0");
            prefs_ed.putString(PREFS_PREV_PLAYER_KEY, PREFS_PREV_NOTHING); 
        }
        else
        {
            Log.w("onPause", "some players, " + current_game.playerCount());
            for(int player_index = 0; player_index < current_game.playerCount(); ++player_index)
            {
                player_name_buffer.append(current_game.getPlayer(player_index).getName());

                Log.w("onPause", "===============================================");
                Log.w("onPause", "storing a player: " + current_game.getPlayer(player_index).getName());
                player_name_buffer.append(PREFS_PREV_DIVIDER);
                
                Log.w("onPause", "storing a player: " + current_game.getPlayer(player_index).getWonder().ordinal());
                prefs_ed.putInt(current_game.getPlayer(player_index).getName(), current_game.getPlayer(player_index).getWonder().ordinal());
            }
            Log.w("onPause", "===============================================");
            String players_encoded  = player_name_buffer.toString();
            
            Log.w("onPause", "players_encoded: " +     players_encoded);
            prefs_ed.putString(PREFS_PREV_PLAYER_KEY, players_encoded);
        }
        
        prefs_ed.commit();
    }
    
    public void loadPrevPlayers()
    {
        prefs        = getSharedPreferences(PREF_KEY, MODE_PRIVATE); 
        
        Log.w("loadPrevPlayers", "BEGIN loadPrevPlayers ----------------------------------"); 
        String prev_player_string = prefs.getString(PREFS_PREV_PLAYER_KEY, PREFS_PREV_NOTHING);
        
        Log.w("loadPrevPlayers", "prev_player_string: " + prev_player_string); 
        
        if(!prev_player_string.equals(PREFS_PREV_NOTHING))
        {    
            String[] players = prev_player_string.split(PREFS_PREV_DIVIDER);

            Log.w("loadPrevPlayers", "==============================================="); 
            Log.w("loadPrevPlayers", "some players: " + players.length);
            
            for(String player : players)
            {
                Log.w("loadPrevPlayers", "===============================================");
                Log.w("loadPrevPlayers", "loading a player: " + player );
                Log.w("loadPrevPlayers", "loading a player: " + WONDER.toString(wonder_list[prefs.getInt(player, 0)]));
                addPlayer(player, wonder_list[prefs.getInt(player, 0)]);
            }
        }
        else
        {
            Log.w("loadPrevPlayers", "no players, it equals NO PLAYERS"); 
        }
    }
    
    
    /**
     * Called when user wants to add another player to the game
     * Displays the necessary prompt to capture data necessary for a new player to be made
     */
    private void addPlayerPrompt() {
        final AlertDialog.Builder alert      = new AlertDialog.Builder(this);
        final View playerPromptView          = factory.inflate(R.layout.player_dialog, null);
        final Spinner spinnerWonder          = (Spinner) playerPromptView.findViewById(R.id.wonder_spinner);
        final AutoCompleteTextView inputName = (AutoCompleteTextView) playerPromptView.findViewById(R.id.player_name);
        
//        if(!addedNewPlayerSinceLastNameCache)
//        {
            player_names = dbm.getPlayerNames(); 
//        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, player_names);
        
        inputName.setAdapter(adapter);
        
        inputName.requestFocus();
        alert.setTitle("Add Player");
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
        
        alert.setIcon(this.getResources().getDrawable(R.drawable.content_new));
        
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


    /**
     * Called after user enters player creation data
     * Adds player to the current game
     */
    private void addPlayer(String name, WONDER wonder) { 
        
        int[] status = dbm.insertPlayer(name);

        //if the player has never been used before
        if(status[0] >= 0)
        {
            current_game.addPlayer(name, wonder, status[1], current_game.expanded_science);
        }
        else
        //if theres one set to not display
        if(status[0] == -2)
        {    
            promptForOverride(name, wonder, status[1], current_game.expanded_science);
        }
        else 
        //if theres one already and its displayable
        if(status[0] == -1)
        {
            current_game.addPlayer(name, wonder, status[1], current_game.expanded_science);
        }
        
        notifyAllAdapters();
        
    }


    private void promptForOverride(final String name, final WONDER wonder, final  int pid, final  boolean expanded_science) { 
        final DatabaseManager dbm_final = dbm;
        
        Dialog temp = new AlertDialog.Builder(this)
        .setTitle("Name Conflict")
        .setMessage("There is a previously deteleted player named " + name + ".")
        .setPositiveButton("Resume Existing",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dbm_final.setPlayerDisplayable(pid, true);
                }
            })
        .setNegativeButton("Cancel",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int whichButton) {

                    Toast.makeText(ctx, "Try again. Each player must have a unique name.",Toast.LENGTH_LONG).show();
                }
            }).create();
        
        temp.setOnDismissListener(new OnDismissListener() {
        
        @Override
        public void onDismiss(DialogInterface dialog) {
        }
        });
        temp.show();
        
    }


    /**
     * Called after user selects and confirms player for removal
     * Remove player to the current game
     */
    private void removePlayer(int position) {
        current_game.removePlayer(position);
        notifyAllAdapters();
    }


    /**
     * Called whenever a global state change requires app-wide update, such as new player or removal of player
     * Notifies all listview adapters that their displays need to be regen'd
     */
    public void notifyAllAdapters() {
        nameAdapter.notifyDataSetChanged();
        sumAdapter.notifyDataSetChanged();
        
        if(science_adapter != null)
            science_adapter.notifyDataSetChanged();
        
        for (STAGE temp : score_adapter_map.keySet()) {
            score_adapter_map.get(temp).notifyDataSetChanged();
        }
        pagerAdapter.notifyDataSetChanged();
    }


    /**
     * The class that handles the view paging between steps in the app
     * Provides access to: Step 0 - Player Creation
     *                     Step 1 through Step N-1 - Scoring Steps
     *                     Step N - Results Display
     */
    public class SevenWonderAdapter extends PagerAdapter {
     
        @Override
        public int getCount() {
            return NUM_TABS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return STAGE.toString(stages.get(position)).toUpperCase();
        }

        @Override
        public Object instantiateItem(View collection, int position) {

            STAGE this_stage = stages.get(position);
            
            View temp = factory.inflate(R.layout.tabpanel, null);
//            ((ViewPager) collection).removeAllViews();
            ((ViewPager) collection).addView(temp, 0);

            ListView content  = (ListView) temp.findViewById(R.id.pagerContent);
            FrameLayout labelBox = (FrameLayout) temp.findViewById(R.id.labelContent);
            
            if (this_stage == STAGE.PLAYERS) 
            {
                RelativeLayout label =  (RelativeLayout) factory.inflate(R.layout.label_players, null);
                ((TextView)label.findViewById(R.id.rightTitle)).setText("Wonder");
                labelBox.addView(label);
                content.setAdapter(nameAdapter);

                return temp;
            } 
            else
            {
                if(nameAdapter.editMode)
                {
                    nameAdapter.commitSelectedWonders();
                    nameAdapter.toggleEditMode();
                    Button edit_button = (Button) findViewById(R.id.edit_wonder_button);
                    edit_button.setText(R.string.change_wonder_prompt);
                    players_remove_click.setEnabled(true);
                }
                
                if (this_stage == STAGE.RESULTS) 
                {
                    RelativeLayout label =  (RelativeLayout) factory.inflate(R.layout.label_results, null);
                    labelBox.addView(label);
                    content.setAdapter(sumAdapter);
                    return temp;
                }
                if (this_stage == STAGE.SCIENCE && current_game.expanded_science) 
                {
                    RelativeLayout label =  (RelativeLayout) factory.inflate(R.layout.label_science, null);
                    labelBox.addView(label);
                    content.setAdapter(science_adapter);
                    return temp;
                }
                else 
                {
                    RelativeLayout label =  (RelativeLayout) factory.inflate(R.layout.label_players, null);
                    ((TextView)label.findViewById(R.id.rightTitle)).setText("Points");
                    labelBox.addView(label);
                    content.setAdapter(score_adapter_map.get(this_stage));
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

    
    /**
     * The class that handles the display and access to manipulation of the app's players
     * Provides access to: creation of player
     *                     removal of player
     *                     update to players wonder
     */    
    private class PlayerAdapter extends BaseAdapter {

        boolean removeMode;
        boolean editMode;
        ArrayList<CheckBox> removeList;
        ArrayList<Spinner>  spinnerList;
        ArrayList<Boolean>  removeSelectionList;
        
        public PlayerAdapter() {
            removeMode           = false;
            editMode             = false;
            removeList           = new ArrayList<CheckBox>();
            spinnerList          = new ArrayList<Spinner>();
            removeSelectionList  = new ArrayList<Boolean>();
        }
        
        @Override
        public void notifyDataSetChanged ()
        {

            if(editMode)
            {
                spinnerList = new ArrayList<Spinner>();
            }
            else if(removeMode)
            {
                int dif = current_game.playerCount() - removeList.size();
                for(int i = 0; i < dif; ++i)
                        removeSelectionList.add(false);
            }
            super.notifyDataSetChanged();
            
        }

        public void commitSelectedWonders() {
            for(int i = 0; i < spinnerList.size(); ++i)
            {
                int index = spinnerList.get(i).getSelectedItemPosition();
                current_game.getPlayer(i).setWonder(wonder_list[index]);
            }
        }

        public void toggleEditMode() {
            if(!editMode)
            {
                spinnerList = new ArrayList<Spinner>();
            }
            editMode = !editMode;
            this.notifyDataSetChanged();
        }

        public void toggleRemoveMode() {
            if(!removeMode)
            {
                removeList = new ArrayList<CheckBox>();
                removeSelectionList  = new ArrayList<Boolean>();
                for(int i = 0; i < current_game.playerCount(); ++i)
                    removeSelectionList.add(false);
            }
            removeMode = !removeMode;
            this.notifyDataSetChanged();
        }
        
        public void removeSelected() {
            ArrayList<Integer> positionsToRemove = new ArrayList<Integer>();
            for(int i = 0; i < removeSelectionList.size(); ++i)
            {
                if(removeSelectionList.get(i))
                {
                    positionsToRemove.add(i);
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
                tempView = (RelativeLayout) factory.inflate(R.layout.player_item_remove, null);
                
                TextView wonderName = (TextView) tempView.findViewById(R.id.wonderName);
                wonderName.setText(current_game.getPlayer(position).getWonderName());
                
                CheckBox remove = (CheckBox) tempView.findViewById(R.id.removePlayerCheck);
                remove.setChecked(removeSelectionList.get(position));
                remove.setTag(position);
                remove.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        removeSelectionList.set((Integer)buttonView.getTag(), isChecked);
                    }
                });
                remove.setTag(position);
                removeList.add(remove);
            }else if(editMode)
            {
                tempView = (RelativeLayout) factory.inflate(R.layout.player_item_edit, null);

                Spinner wonderSpinner = (Spinner) tempView.findViewById(R.id.wonderName);
                wonderSpinner.setSelection(current_game.getPlayer(position).getWonder().ordinal());
                spinnerList.add(wonderSpinner);
                
            }else
            {
                tempView = (RelativeLayout) factory.inflate(R.layout.player_item, null);
                
                TextView wonderName = (TextView) tempView.findViewById(R.id.wonderName);
                wonderName.setText(current_game.getPlayer(position).getWonderName());
            }

            TextView playerName = (TextView) tempView.findViewById(R.id.playerName);
            playerName.setText(current_game.getPlayer(position).getName());
            
            return tempView;
        }
    }
    
    
    /**
     * The class that handles the display of individual scoring steps
     * Provides access to: input and update of players' step scores
     *                     via text input and buttons for inc and dec
     */    
    private class ScoreAdapter extends BaseAdapter {

        STAGE                                    stage;

        public ScoreAdapter(STAGE _stage) {
            stage             = _stage;
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
          //abandon current focus 
            ViewHolder holder;
            if (convertView == null) {
                Log.d("getV", "convertView is null, pos " + position);
                holder = new ViewHolder();
                convertView = factory.inflate(R.layout.score_item, null);

                holder.val = (EditText) convertView.findViewById(R.id.stepScore);                
                
                holder.name = (TextView) convertView.findViewById(R.id.playerName);
                holder.name.setText(current_game.getPlayer(position).getName()); 
               
                holder.pos = (Button) convertView.findViewById(R.id.pos);
                holder.neg = (Button) convertView.findViewById(R.id.neg);
                
                convertView.setTag(holder);
            } else {
                Log.w("getV", "convertView is not null, pos " + position);
                holder = (ViewHolder) convertView.getTag();
                holder.name.setText(current_game.getPlayer(position).getName()); 
                holder.val = (EditText) convertView.findViewById(R.id.stepScore);     
            }
//                        
            final int f_position = position;
            
            //Fill EditText with the value you have in data source
            int score_item = current_game.getPlayerStageScore(position, stage);
            Log.w("getV", "stage_list = " + stage.toString());
            Log.w("getV", "player_pos = " + position);
            Log.w("getV", "score_item = " + score_item);
            Log.w("getV", "-----------------------------------------");
            if(score_item != 0)
                holder.val.setText(score_item + "");
            else
                holder.val.setText(""); 
                
                
//            holder.val.setId(position);

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
                int pos = f_position;
                EditText scorebox = score;
                
                public void afterTextChanged(Editable s) {
                    
                    int value;
                    if (scorebox.getText().toString().equals("") || scorebox.getText().toString().equals("-"))
                    {
                        value = 0;
                    }else
                    if (scorebox.getText().toString().equals("0") )
                    {
                        value = 0;
                        scorebox.setText("");
                    }
                    else 
                    {  
                        try 
                        {
                            value = Integer.parseInt(scorebox.getText().toString());
                        }
                        catch(Exception e)
                        {
                            scorebox.setText("");
                            value = 0;
                        }
                    }
                    current_game.setPlayerStageScore(pos, stage, value);
//                    Log.w("setPlayerStageScore", "scbox = " + scorebox.getId()); 
//                    Log.w("setPlayerStageScore", "stage = " + stage.toString());
//                    Log.w("setPlayerStageScore", "pos   = " + pos);
//                    Log.w("setPlayerStageScore", "value = " + value);
//                    Log.w("setPlayerStageScore", "=========================================");
                }      
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });

//            convertView.setTag(holder);
            return convertView;
        }
    }

    
    /**
     * The class that handles the display of individual scoring steps
     * Provides access to: input and update of players' step scores
     *                     via text input and buttons for inc and dec
     */    
    private class ScienceAdapter extends BaseAdapter {

        STAGE                   stage;

        public ScienceAdapter() {
            stage              = STAGE.SCIENCE;
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

            final ScienceHolder science;
            if (convertView == null) {
                
                //Log.d("SCIENCE", "convertView is null, SCIENCE pos " + position);
                convertView = factory.inflate(R.layout.science_item, null);
                science = new ScienceHolder((EditText) convertView.findViewById(R.id.numCog),
                                            (EditText) convertView.findViewById(R.id.numCompass),
                                            (EditText) convertView.findViewById(R.id.numTablet),
                                            (EditText) convertView.findViewById(R.id.numWild));
      
                science.name    = (TextView) convertView.findViewById(R.id.playerName);
                science.name.setText(current_game.getPlayer(position).getName()); 
               
                science.pos = (Button) convertView.findViewById(R.id.pos);
                science.neg = (Button) convertView.findViewById(R.id.neg);
                
                convertView.setTag(science);
            } else {
                //Log.w("getV", "convertView is not null, pos " + position);
                science = (ScienceHolder) convertView.getTag();
                science.name.setText(current_game.getPlayer(position).getName()); 
                science.cog = (EditText) convertView.findViewById(R.id.numCog);
                science.compass = (EditText) convertView.findViewById(R.id.numCompass);
                science.tablet  = (EditText) convertView.findViewById(R.id.numTablet);
                science.wild    = (EditText) convertView.findViewById(R.id.numWild);
                Log.w("getV", "1111111111111111111111 JUST RETURNING WUT " + position); 
            }
                        
            final int f_position = position;
            

            science.pos.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    int position = current_game.getPlayer(f_position).getSelectedScienceIndex();
                    TextView science_category = science.compass;
                    switch(position)
                    {
                        case COMPASS:
                            science_category = science.compass;
                            break;
                        case COG:
                            science_category = science.cog;
                            break;
                        case TABLET:
                            science_category = science.tablet;
                            break;
                        case WILD:
                            science_category = science.wild;
                            break;
                    }
                    try {
                        int scoreval = Integer.parseInt(science_category.getText().toString());
                        scoreval++;
                        science_category.setText(scoreval + "");
                    }catch(Exception e)
                    {
                        science_category.setText("1");
                    }
                }
            });

           
            science.neg.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    int position = current_game.getPlayer(f_position).getSelectedScienceIndex();
                    TextView science_category = science.compass;
                    switch(position)
                    {
                        case COMPASS:
                            science_category = science.compass;
                            break;
                        case COG:
                            science_category = science.cog;
                            break;
                        case TABLET:
                            science_category = science.tablet;
                            break;
                        case WILD:
                            science_category = science.wild;
                            break;
                    }
                    try {
                        int scoreval = Integer.parseInt(science_category.getText().toString());
                        scoreval--;
                        science_category.setText(scoreval + "");
                    }catch(Exception e)
                    {
                        science_category.setText("");
                    }
                }
                
            });

            if(current_game.getPlayer(position).getScienceScore(COMPASS) != 0)
                science.compass.setText(current_game.getPlayer(position).getScienceScore(COMPASS) + "");
            else
                science.compass.setText("");
            if(current_game.getPlayer(position).getScienceScore(WILD) != 0)
                science.wild.setText(current_game.getPlayer(position).getScienceScore(WILD) + "");
            else
                science.wild.setText("");
            if(current_game.getPlayer(position).getScienceScore(COG) != 0)
                science.cog.setText(current_game.getPlayer(position).getScienceScore(COG) + "");
            else
                science.cog.setText("");
            if(current_game.getPlayer(position).getScienceScore(TABLET) != 0)
                science.tablet.setText(current_game.getPlayer(position).getScienceScore(TABLET) + "");
            else
                science.tablet.setText("");

            science.cog.addTextChangedListener(getScienceWatcher(science.cog, position, COG));
            science.compass.addTextChangedListener(getScienceWatcher(science.compass, position, COMPASS));
            science.tablet.addTextChangedListener(getScienceWatcher(science.tablet, position, TABLET));
            science.wild.addTextChangedListener(getScienceWatcher(science.wild, position, WILD));

            science.cog.setOnFocusChangeListener(getScienceListner(science.cog, position, COG));
            science.compass.setOnFocusChangeListener(getScienceListner(science.compass, position, COMPASS));
            science.tablet.setOnFocusChangeListener(getScienceListner(science.tablet, position, TABLET));
            science.wild.setOnFocusChangeListener(getScienceListner(science.wild, position, WILD));
            
            return convertView;
        }
        
        public TextWatcher getScienceWatcher(final EditText box, final int position, final int CATEGORY)
        {
            return new TextWatcher() {
                
                public void afterTextChanged(Editable s) {} 
                
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int value;
                    if(box.getText().toString().equals("") || box.getText().toString().equals("-"))
                    {
                        value = 0;
                    }
                    else
                    if(box.getText().toString().equals("0") )
                    {
                        value = 0;
                        box.setText("");
                    }
                    else 
                    {  
                        try 
                        {
                            value = Integer.parseInt(box.getText().toString());
                        }
                        catch(Exception e)
                        {
                            box.setText("");
                            value = 0;
                        }
                    }
                    current_game.getPlayer(position).setScienceScore(value, CATEGORY);
                }     
            };
        }
      

        public OnFocusChangeListener getScienceListner(final EditText box, final int position, final int CATEGORY)
        {
            return new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus)
                    {
                        current_game.getPlayer(position).setSelectedScienceIndex(CATEGORY);
                    }
                }
            };
        }
    }

    
    /**
     * Minor helper class to retain easy access to list item's UI elements
     */  
    class ViewHolder {
        TextView name;
        EditText val;
        Button pos;
        Button neg;
    } 
    
    
    class ScienceHolder {
        TextView name;
        EditText cog;
        EditText compass;
        EditText tablet;
        EditText wild;
        Button pos;
        Button neg;
        
        public ScienceHolder(EditText _cog, EditText _compass, EditText _tablet, EditText _wild) {
            cog      = _cog;
            compass  = _compass;
            tablet   = _tablet;
            wild     = _wild;
            }
    }


    /**
     * The class that handles the display of the games score results
     * Provides access to: individual step wins
     *                     total score
     *                     player placement
     */  
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
            RelativeLayout tempView = (RelativeLayout) factory.inflate(R.layout.result_item, null);
            TextView name_box       = (TextView) tempView.findViewById(R.id.playerName);
            TextView sum_box        = (TextView) tempView.findViewById(R.id.totalScore);
            TextView position_box   = (TextView) tempView.findViewById(R.id.finishingPosition);
            LinearLayout badge_box  = (LinearLayout) tempView.findViewById(R.id.medalBox);
            
            name_box.setText(current_game.getPlayer(position).getName());
            sum_box.setText(current_game.getPlayer(position).getTotal() + "");
            
            Spannable suffix_span = new SpannableString(current_game.getPlayer(position).getPlace() + current_game.getPlayer(position).getPlaceSuffix());
            int position_length = Integer.toString(current_game.getPlayer(position).getPlace()).length();
            suffix_span.setSpan(new SuperscriptSpan(), position_length, suffix_span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            suffix_span.setSpan(new RelativeSizeSpan(.35f), position_length, suffix_span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            position_box.setText(suffix_span);
            badge_box.setVisibility(View.VISIBLE);

            for (STAGE step_won : current_game.getPlayer(position).getStageWins()) 
            {
                if(stage_medals.containsKey(step_won))
                {
                    ImageView tempIV     = new ImageView(ctx);
                    Bitmap bmp           = ((BitmapDrawable)stage_medals.get(step_won)).getBitmap();
                    Bitmap resizedbitmap = Bitmap.createScaledBitmap(bmp, dp28, dp28, true);
                    tempIV.setImageBitmap(resizedbitmap);
                    badge_box.addView(tempIV);
                }
            }

            if (current_game.getPlayer(position).getPlace() == 1) 
            {
                suffix_span.setSpan(new ForegroundColorSpan(Color.WHITE), position_length, suffix_span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            
                sum_box.setTextColor(Color.parseColor("#ffffff"));
                sum_box.setTypeface(null, Typeface.BOLD);
                name_box.setTypeface(null, Typeface.BOLD);
                position_box.setTextColor(Color.parseColor("#ffffff"));
                tempView.setBackgroundColor(Color.parseColor("#33b5e5"));
            }

            return tempView;
        }
    }
}