package com.aceanuu.swss.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.aceanuu.swss.logic.STAGE;
import com.aceanuu.swss.logic.WONDER;


public class ResultStat {
    public Map<STAGE, Integer>  step_scores;
    public ArrayList<STAGE>     step_wins;
    public WONDER               wonder;
    
    public int                  gid;
    public int                  rid;
    public int                  pid;
    public int                  position;
    public int                  total;
    public int                  players_in_game;
    
    public ResultStat()
    {
        step_scores = new HashMap<STAGE, Integer>();
        step_wins   = new ArrayList<STAGE>();
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append( "GID: " + gid +
                  " RID: " + rid +
                  " PID: " + pid + "\n");
        
        for(STAGE stage : step_scores.keySet())
        {
            sb.append(STAGE.toString(stage) + " = " + step_scores.get(stage) + "\n");
            if(step_wins.contains(stage))
                sb.append(STAGE.toString(stage) + " top scorer!\n");
        }
        sb.append("TOTAL SCORE: " + total + "\n");
        sb.append("Finishing Position: " + position + "\n");
        
        return sb.toString();
    }
}
