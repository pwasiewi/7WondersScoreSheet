package com.aceanuu.swss.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;



public class Game {

    public ArrayList<Stages>  scoring_stages;
    public ArrayList<Player>  player_list;

    
    public Game(){
        player_list    = new ArrayList<Player>();
        scoring_stages = new ArrayList<Stages>(Arrays.asList(Stages.values()));
        scoring_stages.remove(Stages.RESULTS);
        scoring_stages.remove(Stages.PLAYERS);
    }
    
    
    /**
     * Resets all players scores to begin a new game
     *
     */
    public void newGame(){
        for(Player temp_player : player_list)
            temp_player.resetScores();
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
    public void addPlayer(String _name, Wonders _wonder)
    {
        player_list.add(new Player(_name, _wonder));
    }
    
    
    /**
     * Remove an existing player from the game.
     *
     * @param _index  the index of the player to be removed
     */
    public void removePlayer(int _index)
    {
        player_list.remove(_index);
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
        
        for(int i = 0; i < sorted_player_list.size(); ++i)
        {
            sorted_player_list.get(i).setPlace(i+1);
        }
    }
    
    
    /**
     * This method determines which players won 
     * individual steps, ie had the most Military, science, etc
     */ 
    private void generateStepWinners(){

        for (Stages current_step : scoring_stages) {
            
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
    public void setPlayerStageScore(int _index, Stages _stage, int _score)
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
    public int getPlayerStageScore(int _index, Stages _stage)
    {
        return player_list.get(_index).getStageScore(_stage);
    }

}
