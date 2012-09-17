package com.aceanuu.swss.sqlite;

import java.util.ArrayList;
import java.util.Map;

import com.aceanuu.swss.Stages;


public class Result {
    ArrayList<Integer> scores;
    ArrayList<Integer> category_wins;
    int place;
    int total;
    private Map<Stages, Integer> step_scores;
    private Map<Stages, Boolean> step_wins;
    
    public Result(Map<Stages, Integer> _step_scores, Map<Stages, Boolean> _step_wins,  int _place, int _total)
    {
        step_scores = _step_scores;
        step_wins   = _step_wins;
        place       = _place;
        total       = _total;
    }
}
