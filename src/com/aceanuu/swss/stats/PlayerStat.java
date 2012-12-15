package com.aceanuu.swss.stats;

import java.util.HashMap;

import android.database.Cursor;

import com.aceanuu.swss.logic.STAGE;



public class PlayerStat { 
    
    public String              name; 
    public int                 pid;
    public HashMap<Integer, GameStat>      game_list;
    
    public PlayerStat(String _name, int _pid) {
        name                      = _name;  
        pid                       = _pid; 
        game_list                 = new HashMap<Integer, GameStat>();
    }
 
    public void addResultRow(Cursor row)
    {
        int rid      = row.getInt(row.getColumnIndex("S.RID")); 
        int gid      = row.getInt(row.getColumnIndex("S.GID")); 
        int pid      = row.getInt(row.getColumnIndex("S.PID")); 
        int position = row.getInt(row.getColumnIndex("R.POSITION")); 
        int total    = row.getInt(row.getColumnIndex("R.TOTAL"));  
        int score    = row.getInt(row.getColumnIndex("S.SCORE"));
        int medal    = row.getInt(row.getColumnIndex("S.MEDAL")); 
        String cat   = row.getString(row.getColumnIndex("S.CATEGORY"));
        
        GameStat gs;
        
        //if game is already listed
        if(game_list.containsKey(gid))
        {
            gs = game_list.get(gid);
        }
        //if game is not already in playerstat
        else
        {
            gs = new GameStat();
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
