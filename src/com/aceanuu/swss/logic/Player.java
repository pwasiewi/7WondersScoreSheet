package com.aceanuu.swss.logic;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;



public class Player {

    private String               name;
    private Wonders              wonder;
    private Map<Stages, Integer> step_scores;
    private ArrayList<Stages>    step_wins;
    private int                  total;
    private int                  finishing_place;
    private String               finishing_place_string;
    
    public Player(String _name, Wonders _wonder) {
        name            = _name;
        wonder          = _wonder;
        total           = 0;
        finishing_place = 0;
        finishing_place_string = "";
        step_scores     = new TreeMap<Stages, Integer>();
        step_wins       = new ArrayList<Stages>();
    }

    public void resetScores() {
        step_wins       = new ArrayList<Stages>();
        step_scores     = new TreeMap<Stages, Integer>();
        total           = 0;
        finishing_place = 0;
    }
    
    public void clearSums() {
        step_wins       = new ArrayList<Stages>();
        total           = 0;
        finishing_place = 0;
    } 
    
    
    public Wonders getWonder() {
        return wonder;
    }
    public String getWonderName() {
        return wonder.name();
    }
    public void setWonder(Wonders _wonder) {
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

    public void setStageScore(Stages _stage, int _score) {
        step_scores.put(_stage, _score);
    }
    
    public int getStageScore(Stages _stage) {
        if(step_scores.get(_stage) != null)
            return step_scores.get(_stage);
        else
            return 0;
    }
    
    public void computeTotal(){
        total = 0;
        for(Stages temp_key : step_scores.keySet()) {
            total += step_scores.get(temp_key);
        }
    }

    public int getTotal(){
        return total;
    }
    
    public String getPlaceSuffix(){
        StringBuilder place_builder = new StringBuilder();
//        if(finishing_place_string == "")
//        {
            String place_int = Integer.toString(finishing_place);
//            place_builder.append(place_int);
            
            char ones_digit = place_int.charAt(place_int.length() - 1);
            
            if(finishing_place <10 || finishing_place > 20)
            {
                if(ones_digit == '1')
                    place_builder.append("st");
                else if(ones_digit == '2')
                    place_builder.append("nd");
                else if(ones_digit == '3')
                    place_builder.append("rd");
            }else
                place_builder.append("th");
            
            finishing_place_string = place_builder.toString();
//        }
        return finishing_place_string;
    }
    
    public void setStepWin(Stages _stage){
        step_wins.add(_stage);
    }


    public void setPlace(int i) {
        finishing_place = i;
    }
    
    public int getPlace() {
        return finishing_place;
    }

    public ArrayList<Stages> getStageWins() {
        return step_wins;
    }
}
