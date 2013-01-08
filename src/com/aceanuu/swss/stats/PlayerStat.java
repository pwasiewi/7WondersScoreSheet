package com.aceanuu.swss.stats;

import java.util.HashMap;

import android.database.Cursor;
import android.util.Log;

import com.aceanuu.swss.logic.STAGE;



public class PlayerStat { 
    
    public String              name; 
    public int                 pid;
    public HashMap<Integer, ResultStat>      game_list;
    public Long	               datelong;
    public String              misc_stat;
    
    public PlayerStat(String _name, int _pid) {
        name                      = _name;  
        pid                       = _pid; 
        game_list                 = new HashMap<Integer, ResultStat>();
    }
 
    public String toLabelString()
    {
    	return misc_stat + " - " + name;
    }
    
    public void addResultRow(Cursor row)
    {
    	Log.i("addResultRow", row.toString());
        int rid      = row.getInt(row.getColumnIndex("RID")); 
        int gid      = row.getInt(row.getColumnIndex("GID")); 
        int pid      = row.getInt(row.getColumnIndex("PID")); 
        int position = row.getInt(row.getColumnIndex("POSITION")); 
        int total    = row.getInt(row.getColumnIndex("TOTAL"));  
        int score    = row.getInt(row.getColumnIndex("SCORE"));
        int medal    = row.getInt(row.getColumnIndex("MEDAL")); 
        String cat   = row.getString(row.getColumnIndex("CATEGORY"));
        
        ResultStat gs;
        
        //if game is already listed
        if(game_list.containsKey(gid))
        {
            gs = game_list.get(gid);
        }
        //if game is not already in playerstat
        else
        {
            gs = new ResultStat();
            game_list.put(gid, gs);
            gs.rid = rid;
            gs.pid = pid;
            gs.gid = gid;
            gs.position = position;
            gs.total    = total; 
        }
        
        STAGE stage = STAGE.convertStringToEnum(cat);
        gs.step_scores.put(stage, score);
        if(medal == 1)
            gs.step_wins.add(stage);
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Score for " + name + "\n");
        
        for(int gid : game_list.keySet())
        {
            sb.append(game_list.get(gid).toString() + "\n");
        }
        
        return sb.toString();
    }

}
