package com.aceanuu.swss.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;



public class Player {

    public final static int TABLET  = 0;
    public final static int COG     = 1;
    public final static int COMPASS = 2;
    public final static int WILD    = 3;
    
    private String              name;
    private WONDER              wonder;
    private Map<STAGE, Integer> step_scores;
    private ArrayList<STAGE>    step_wins;
    private int                 total;
    private int                 finishing_place;
    public  int[]               science;
    public  int                 selected_science_index;
    public  boolean             expanded_science;
    
    public Player(String _name, WONDER _wonder, boolean _expanded_science) {
        name                      = _name;
        wonder                    = _wonder;
        total                     = 0;
        finishing_place           = 0;
        step_scores               = new TreeMap<STAGE, Integer>();
        step_wins                 = new ArrayList<STAGE>();
        science                   = new int[] {0,0,0,0};
        selected_science_index    = 0;
        step_scores.put(STAGE.SCIENCE, 0); 
        expanded_science          = _expanded_science;
    }

    public void resetScores() {
        step_wins                 = new ArrayList<STAGE>();
        step_scores               = new TreeMap<STAGE, Integer>();
        step_scores.put(STAGE.SCIENCE, 0); 
        total                     = 0;
        finishing_place           = 0;
        selected_science_index    = 0;
        science[0]                = 0;
        science[1]                = 0;
        science[2]                = 0;
        science[3]                = 0;
    }
    
    
    public void setExpandedScience(boolean t)
    {
        expanded_science = t;
    }
    
    public void clearSums() {
        step_wins                 = new ArrayList<STAGE>();
        total                     = 0;
        finishing_place           = 0;
    } 

    public void setScienceScore(int value, int CATEGORY) {
        science[CATEGORY]  = value;
    }
    
    public int getScienceScore(int CATEGORY) {
        return science[CATEGORY];
    }
    
    public WONDER getWonder() {
        return wonder;
    }
    
    public void setSelectedScienceIndex(int INDEX) {
        selected_science_index = INDEX;
    }
    
    public int getSelectedScienceIndex() {
        return selected_science_index;
    }
    
    public String getWonderName() {
        return WONDER.toString(wonder);
    }
    public void setWonder(WONDER _wonder) {
        wonder = _wonder;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String _name) {
        name = _name;
    }
    
    @Override
    public String toString()
    {
        return name + " as " + wonder.name();
    }

    public void setStageScore(STAGE _stage, int _score) {
        step_scores.put(_stage, _score);
    }
    
    public int getStageScore(STAGE _stage) {
        if(_stage == STAGE.SCIENCE && expanded_science)
            getSciencePoints();
        if(step_scores.get(_stage) != null)
            return step_scores.get(_stage);
        else
            return 0;
    }
    
    public void computeTotal(){
        total = 0;
        for(STAGE temp_key : step_scores.keySet()) {
            total += getStageScore(temp_key);
        }
    }

    public int getTotal(){
        return total;
    }
    
    public String getPlaceSuffix(){
        String place_int = Integer.toString(finishing_place);
        char ones_digit = place_int.charAt(place_int.length() - 1);
        
        if(finishing_place < 10 || finishing_place > 20)
        {
            if(ones_digit == '1')
                return "st";
            else if(ones_digit == '2')
                return "nd";
            else if(ones_digit == '3')
                return "rd";
            else
                return "th";
        }else
            return "th";
    }
    
    public void setStepWin(STAGE _stage){
        step_wins.add(_stage);
    }

    public void setPlace(int i) {
        finishing_place = i;
    }
    
    public int getPlace() {
        return finishing_place;
    }

    public ArrayList<STAGE> getStageWins() {
        return step_wins;
    }

    public int getSciencePointsHelper(int[] outcome){
        int sciencePoints = 0;
        int leastSymbol = Math.min(Math.min(outcome[COG], outcome[COMPASS]), outcome[TABLET]);
        
        sciencePoints += leastSymbol*7;
        sciencePoints += outcome[COG]*outcome[COG];
        sciencePoints += outcome[COMPASS]*outcome[COMPASS];
        sciencePoints += outcome[TABLET]*outcome[TABLET];
        return sciencePoints;
    }
    
    public int getSciencePoints() {
        
        int tablet  = science[TABLET];
        int cog     = science[COG];
        int compass = science[COMPASS];
        int wilds   = science[WILD];
        
        LinkedList<int[]> scores = new LinkedList<int[]>();
        
        //load up the list with initial value
        scores.add(new int[] {tablet, cog, compass});
        
        for(int wild_lvl = 0; wild_lvl < wilds; ++wild_lvl)
        {
            int num_to_pop = (int) Math.pow(3, wild_lvl);
            
            for(int popped = 0; popped < num_to_pop; ++popped)
            {
                int[] values   = scores.remove();
                
                tablet         = values[TABLET];
                cog            = values[COG];
                compass        = values[COMPASS];
                
                scores.add(new int[] {tablet + 1, cog, compass});
                scores.add(new int[] {tablet, cog + 1, compass});
                scores.add(new int[] {tablet, cog, compass + 1});
            }
        }
        
        int maxValue = 0;
        for(int[] v : scores)
        {
            int val = getSciencePointsHelper(v);
            if(val > maxValue)
                maxValue = val;
        }
        
        step_scores.put(STAGE.SCIENCE, maxValue);
        return maxValue;
    }

}
