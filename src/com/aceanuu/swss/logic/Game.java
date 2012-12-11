package com.aceanuu.swss.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import android.util.Log;

import com.aceanuu.swss.sqlite.DatabaseManager;

public class Game {

    public ArrayList<STAGE>   scoring_stages;
    public ArrayList<Player>  player_list; 
    private int               GID;
    public ArrayList<Integer> player_pids;
    public ArrayList<Integer> player_pids_at_last_save;
    public boolean                      expanded_science;
    public boolean                      leaders_enabled;
    public boolean                      cities_enabled;
    
    public Game(){
        player_pids    = new ArrayList<Integer>();
        player_pids_at_last_save = new ArrayList<Integer>();
        player_list    = new ArrayList<Player>();
        scoring_stages = new ArrayList<STAGE>(Arrays.asList(STAGE.values()));
        scoring_stages.remove(STAGE.RESULTS);
        scoring_stages.remove(STAGE.PLAYERS); 
        setGID(-1);
    }
    
    
    /**
     * Resets all players scores to begin a new game
     *
     */
    public void newGame(){ 
        setGID(-1);
        player_pids    = new ArrayList<Integer>();
        player_pids_at_last_save = new ArrayList<Integer>();
        for(Player temp_player : player_list)
        {
            player_pids.add(temp_player.pid);
            temp_player.resetScores();
            temp_player.setRID(-1);
        }
    }

    /**
     * Returns the requested player to the game.
     *
     * @param _index   the index of the player
     * @return the Player at the given index
     */
    public Player getPlayer(int _index)
    {
        return player_list.get(_index);
    }
    
    
    /**
     * Add a new player to the game.
     *
     * @param _name   the name of the player
     * @param _wonder the Wonder enum of the player's wonder 
     */
    public void addPlayer(String _name, WONDER _wonder, int _pid, Boolean _expanded_science)
    {
        player_list.add(new Player(_name, _wonder, _pid, _expanded_science));
        player_pids.add(_pid);
    }
    
    
    /**
     * Remove an existing player from the game.
     *
     * @param _index  the index of the player to be removed
     */
    public void removePlayer(int _index)
    {
        player_list.remove(_index);
        player_pids.remove(_index);
    }

    
    /**
     * Get the number of players in the game
     *
     * @return the number of players
     */
    public int playerCount() {
        return player_list.size();
    }

    
    /**
     * Produces all end-game data
     */ 
    public void generateResults() {
        for(Player temp_player : player_list)
            temp_player.clearSums();
        determineWinner();
        generateStepWinners();
    }
    

    /**
     * This method sums up the results of the game
     * and determines the final places for each player
     */ 
    public void determineWinner() {
                                           //index   //score
        for(Player temp_player : player_list) {
            temp_player.computeTotal();
        }
        
        ArrayList<Player> sorted_player_list = new ArrayList<Player>();
        for(Player temp_player : player_list)
        {
            sorted_player_list.add(temp_player);
        }
        
        Collections.sort(sorted_player_list, new Comparator<Player>() {

            @Override
            public int compare(Player lhs, Player rhs) {
                return  rhs.getTotal() - lhs.getTotal();
            }
        });
        
        int previous_total = -999999;
        int previous_place = 0;
        for(int i = 0; i < sorted_player_list.size(); ++i)
        {
            if(previous_total == sorted_player_list.get(i).getTotal())
            {
                sorted_player_list.get(i).setPlace(previous_place);
            }
            else
            {
                previous_place += 1;
                previous_total = sorted_player_list.get(i).getTotal();
                sorted_player_list.get(i).setPlace(previous_place);
            }
        }
    }
    
    
    /**
     * This method determines which players won 
     * individual steps, ie had the most Military, science, etc
     */ 
    private void generateStepWinners(){

        for (STAGE current_step : scoring_stages) {
            
            int step_max = -9999999;
            ArrayList<Player> stage_winners = new ArrayList<Player>();
            
            for (Player this_player : player_list) 
            {
                int player_step_score = this_player.getStageScore(current_step);
                if (step_max < player_step_score) 
                {
                    step_max = player_step_score;
                    stage_winners.clear();
                    stage_winners.add(this_player);
                } 
                else if (step_max == player_step_score) 
                {
                    stage_winners.add(this_player);
                }
            }
            
            if(step_max == 0)
                stage_winners.clear();
            
            for(Player step_winning_player : stage_winners)
            {
                step_winning_player.setStepWin(current_step);
            }
        }
    }
    
    
    /**
     * Add one players score for a stage
     *
     * @param _index  the index of the player to be scored
     * @param _index  the stage of the round to be scored
     * @param _index  the stage score for the player
     */
    public void setPlayerStageScore(int _index, STAGE _stage, int _score)
    {
        player_list.get(_index).setStageScore(_stage, _score);
    }
    
    /**
     * Add one players score for a stage
     *
     * @param _index  the index of the player to be scored
     * @param _index  the stage of the round to be scored
     * @param _index  the stage score for the player
     */
    public int getPlayerStageScore(int _index, STAGE _stage)
    {
        return player_list.get(_index).getStageScore(_stage);
    }
    
    public String generateExpansionCode()
    {
        StringBuilder str  = new StringBuilder();

        if(leaders_enabled)
            str.append("leaders");
        
        if(leaders_enabled && cities_enabled)
            str.append(",");
        
        if(cities_enabled)
            str.append("cities");
        
        return str.toString();
    }
    

    /**
     * Saves the game results and players to the database
     * @param dbm 
     *
     */
    public void saveGame(DatabaseManager dbm)
    {
        //if hasn't been saved before
        if(this.getGID() == -1)
        {
            Log.d("saveGame", "insertGame");
            setGID(dbm.insertGame(this));
            
            for(Integer pid : player_pids)
                player_pids_at_last_save.add(pid);
        }
        else
        {
            ArrayList<Integer> pids_to_remove = new ArrayList<Integer>();
            for(Integer last_pid : player_pids_at_last_save)
            {
                if(!player_pids.contains(last_pid))
                    pids_to_remove.add(last_pid);
            }
            
            Log.d("saveGame", "modifyGame");
            dbm.modifyGame(this);
            
            for(Integer pid : pids_to_remove)
            {
                Log.d("saveGame", "removeResult PID: " + pid + ", GID: "+  GID );
                dbm.removeResult(pid, GID);
            }
            
            player_pids_at_last_save.clear();
            for(Integer pid : player_pids)
                player_pids_at_last_save.add(pid);
        } 
    }
 

    public void sortByScores() {
        Collections.sort(player_list, new Comparator<Player>()  {

            @Override
            public int compare(Player lhs, Player rhs) {
                return rhs.getTotal() - lhs.getTotal();
            } 
        });
    }


    public void updatePlayerScienceScoring(boolean _expanded_science) {
        expanded_science = _expanded_science;
        for(Player player : player_list)
        {
            player.setExpandedScience(expanded_science);
        }
        
    }


    public void computePlayersScienceScores() {
        
        for(Player player : player_list)
        {
            player.getSciencePoints();
        }
        
        
    }


    public void clearScoresCategory(STAGE debt) { 

        for(Player player : player_list)
        {
            player.setStageScore(debt, 0);
        }
        
    }


    public int getGID() {
        return GID;
    }


    public void setGID(int gID) {
        GID = gID;
    }

}
