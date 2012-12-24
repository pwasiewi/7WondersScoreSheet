package com.aceanuu.swss.sqlite;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aceanuu.swss.logic.Game;
import com.aceanuu.swss.logic.Player;
import com.aceanuu.swss.logic.STAGE;
import com.aceanuu.swss.stats.PlayerStat;
import com.aceanuu.swss.stats.ResultStat;

//copy database 
//adb shell
//su
//cat /data/data/com.aceanuu.swss/databases/SEVEN_WONDERS_SCORE_SHEET_DATABASE > /sdcard/SWSSDB 

//select a game from a GID
//SELECT * FROM PLAYER P, RESULT R, GAME G, SCORE S WHERE G.GID = 3 AND R.GID = G.GID AND S.RID = R.RID AND R.PID = P.PID

//average total score from a PID
//SELECT avg(S.SCORE) FROM PLAYER P, RESULT R, GAME G, SCORE S WHERE P.PID = 1 AND R.GID = G.GID AND S.RID = R.RID AND R.PID = P.PID AND S.CATEGORY = "RESULTS"

//wonder that is the highest scoring in science
//SELECT  R.EMPIRENAME FROM PLAYER P, RESULT R, GAME G, SCORE S
//WHERE R.GID = G.GID AND S.RID = R.RID AND R.PID = P.PID AND S.CATEGORY = "SCIENCE" ORDER BY S.SCORE DESC LIMIT 1

public class DatabaseManager extends SQLiteOpenHelper  {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 29;
 
    // Database Name
    private static final String DATABASE_NAME = "SEVEN_WONDERS_SCORE_SHEET_DATABASE";
 
    // Contacts table name
    private static final String TABLE_PLAYER     = "PLAYER";
        private static final String PID          = "PID";
        private static final String PNAME        = "NAME";
        private static final String PDISPLAY     = "DISPLAY";
        private static final String PTIME        = "TIME";
        
    private static final String TABLE_RESULT     = "RESULT";
        private static final String RID          = "RID";
        private static final String RPLAYERID    = "PID";
        private static final String RGAMEID      = "GID";
        private static final String RWONDERNAME  = "WONDERNAME";
        private static final String RCOGS        = "COGS";
        private static final String RCOMPASS     = "COMPASS";
        private static final String RTABLET      = "TABLET";
        private static final String RWILD        = "WILD";
        private static final String RTOTAL       = "TOTAL";
        private static final String RPOSITION    = "POSITION"; 
        private static final String RTIME        = "TIME";
        
    private static final String TABLE_SCORE      = "SCORE";
        private static final String SGAMEID      = "GID";
        private static final String SSCOREID     = "SID";
        private static final String SRESULTSID   = "RID";
        private static final String SPLAYERID    = "PID";
        private static final String SCATEGORY    = "CATEGORY";
        private static final String SSCORE       = "SCORE";
        private static final String SMEDAL       = "MEDAL";

    private static final String TABLE_GAME       = "GAME";
        private static final String GID          = "GID";
        private static final String GTIME        = "TIME";
        private static final String GPCOUNT      = "PLAYERCOUNT";
        private static final String GEXPCODE     = "EXPANSIONCODE";
        private static final String GEXPANDEDSCIENCE = "EXPANDEDSCIENCE";
     
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); 
        Log.d("DatabaseManager", "CREATING DatabaseManager");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("onCreate", "CREATING TABLES AND SHIT");
        String CREATE_PLAYER_TABLE = "CREATE TABLE " + TABLE_PLAYER 
                + "("
                + PID   + " INTEGER PRIMARY KEY AUTOINCREMENT ," 
                + PNAME + " TEXT,"
                + PDISPLAY    + " INTEGER,"
                + PTIME + " INTEGER" 
                + ");";
        db.execSQL(CREATE_PLAYER_TABLE);
        

        String CREATE_TABLE_RESULT = "CREATE TABLE " + TABLE_RESULT 
                + "("
                + RID   + " INTEGER PRIMARY KEY AUTOINCREMENT ," 
                + RPLAYERID    +  " INTEGER,"
                + RGAMEID    +  " INTEGER,"
                + RWONDERNAME    + " TEXT,"
                + RCOGS    + " INTEGER,"
                + RCOMPASS    + " INTEGER,"
                + RTABLET    + " INTEGER,"
                + RWILD    + " INTEGER,"
                + RTOTAL    + " INTEGER,"
                + RPOSITION+ " INTEGER," 
                + RTIME + " INTEGER," 
                + "FOREIGN KEY(" + RPLAYERID + ") REFERENCES " + TABLE_PLAYER + "(" + PID + ")," 
                + "FOREIGN KEY(" + RGAMEID + ") REFERENCES " + TABLE_GAME + "(" + GID + ")" 
                + ");";
        db.execSQL(CREATE_TABLE_RESULT);

        String CREATE_TABLE_SCORE = "CREATE TABLE " + TABLE_SCORE 
                + "("
                + SGAMEID   + " INTEGER," 
                + SRESULTSID     +  " INTEGER,"
                + SPLAYERID     +  " INTEGER,"
                + SCATEGORY    + " TEXT,"
                + SSCORE    + " INTEGER,"
                + SMEDAL    + " INTEGER,"
                + "FOREIGN KEY(" + SRESULTSID + ") REFERENCES " + TABLE_RESULT + "(" + RID + ")" 
                + "FOREIGN KEY(" + SPLAYERID + ") REFERENCES " + TABLE_PLAYER + "(" + PID + ")"
                + "PRIMARY KEY(" + SGAMEID +", " + SRESULTSID +", " + SPLAYERID +", " +SCATEGORY + ")"
                + ");";
        db.execSQL(CREATE_TABLE_SCORE);
        

        String CREATE_TABLE_GAME = "CREATE TABLE " + TABLE_GAME 
                + "("
                + GID   + " INTEGER PRIMARY KEY AUTOINCREMENT," 
                + GTIME    + " INTEGER,"
                + GPCOUNT    + " INTEGER,"
                + GEXPCODE    + " TEXT,"
                + GEXPANDEDSCIENCE  + " INTEGER"
                + ");";
        db.execSQL(CREATE_TABLE_GAME);
    }

    
    
    
    
    
    public int insertGame(Game gametoSave)
    { 
        Log.d("insertGame", "insertGame");
        SQLiteDatabase db = this.getWritableDatabase();

//        + GID   + " INTEGER PRIMARY KEY AUTOINCREMENT," 
//        + GTIME    + " TEXT,"
//        + GPCOUNT    + " INTEGER,"
//        + GEXPCODE    + " TEXT" 
        long time = System.currentTimeMillis();
      ContentValues values = new ContentValues();
      values.put(GEXPCODE, "");  
      values.put(GPCOUNT, gametoSave.playerCount());  
      if(gametoSave.expanded_science)
          values.put(GEXPANDEDSCIENCE, 1);
      else
          values.put(GEXPANDEDSCIENCE, 0);
      values.put(GTIME, time);  
      
      int GID = 0;
      
      // Inserting Row 
      db.beginTransaction();
      try {
          // Inserting Row
          db.insert(TABLE_GAME, null, values);     

          GID = getMaxID(TABLE_GAME);
          
          for(int i = 0; i < gametoSave.playerCount(); ++i)
          {
              int rid = insertResult(gametoSave.getPlayer(i), GID, time);
              gametoSave.getPlayer(i).setRID(rid);
          }
          db.setTransactionSuccessful();
      } finally {
          db.endTransaction();
      } 
     
      db.close(); // Closing database connection
      return GID;
        
    }
    


    public void modifyGame(Game game) { 
        Log.d("modifyGame", "modifyGame");

        SQLiteDatabase db = this.getWritableDatabase();

//        + GID   + " INTEGER PRIMARY KEY AUTOINCREMENT," 
//        + GTIME    + " TEXT,"
//        + GPCOUNT    + " INTEGER,"
//        + GEXPCODE    + " TEXT"
        long time = System.currentTimeMillis();
      ContentValues values = new ContentValues();
      values.put(GEXPCODE, game.generateExpansionCode());  
      values.put(GPCOUNT, game.playerCount());  
      if(game.expanded_science)
          values.put(GEXPANDEDSCIENCE, 1);
      else
          values.put(GEXPANDEDSCIENCE, 0);
      values.put(GTIME, time);  

      db.beginTransaction();
          try {
          // Inserting Row
          db.update(TABLE_GAME, values, GID + " = " + game.getGID(), null);
    
          int GID = getMaxID(TABLE_GAME);
          
          for(int i = 0; i < game.playerCount(); ++i)
          { 
              Log.d("modifyGame", "about to insertResult with RID of " + game.getPlayer(i).getRID());
              if(game.getPlayer(i).getRID() == -1)
              {
                  int rid = insertResult(game.getPlayer(i), GID, time);
                  game.getPlayer(i).setRID(rid);
              }
              else
                  updateResult(game.getPlayer(i), GID, time);
          }
          db.setTransactionSuccessful();
      } finally {
          db.endTransaction();
      }
      
      db.close(); // Closing database connection
      
    }

    public int insertResult(Player p1, int gid, long time) {
        Log.d("insertResult", "insertResult");
        SQLiteDatabase db = this.getWritableDatabase();

//      + PID   + " INTEGER PRIMARY KEY AUTOINCREMENT ," 
//      + PNAME + " TEXT,"
//      + PDISPLAY    + " INTEGER,"
//      + PCREATEDDATE+ " TEXT" 
        int id = 0;
        
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put( RPLAYERID   , p1.pid);
            values.put( RGAMEID     , gid);
            values.put( RWONDERNAME , p1.getWonderName());
            values.put( RCOGS        , p1.getScienceScore(0));
            values.put( RCOMPASS     , p1.getScienceScore(1));
            values.put( RTABLET     , p1.getScienceScore(2));
            values.put( RWILD      , p1.getScienceScore(3));
            values.put( RTOTAL    , p1.step_scores.get(STAGE.RESULTS));
            values.put( RPOSITION, p1.getPlace());
            values.put( RTIME, System.currentTimeMillis());  
         
           // Inserting Row
           db.insert(TABLE_RESULT, null, values);
           
    
           id = getMaxID(TABLE_RESULT);
          
           for(STAGE stage : p1.step_scores.keySet())
           {
               ContentValues score = new ContentValues();
               score.put(SRESULTSID, id);
               score.put(SGAMEID, gid);
               score.put(SPLAYERID, p1.pid);
               score.put(SCATEGORY, stage.toString());
               score.put(SSCORE, p1.step_scores.get(stage));
               if(p1.step_wins.contains(stage))
                   score.put(SMEDAL, 1);
               else
                   score.put(SMEDAL, 0);
               db.insert(TABLE_SCORE, null, score);
           }
    
           db.setTransactionSuccessful();
      } finally {
          db.endTransaction();
      }
      
      return id;
    }
    

    public int updateResult(Player p1, int gid, long time) {
        Log.d("updateResult", "updateResult");
        SQLiteDatabase db = this.getWritableDatabase();

//      + PID   + " INTEGER PRIMARY KEY AUTOINCREMENT ," 
//      + PNAME + " TEXT,"
//      + PDISPLAY    + " INTEGER,"
//      + PCREATEDDATE+ " TEXT" 
        int id = 0;
        
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put( RPLAYERID   , p1.pid);
            values.put( RGAMEID     , gid);
            values.put( RWONDERNAME , p1.getWonderName());
            values.put( RCOGS       , p1.getScienceScore(0));
            values.put( RCOMPASS    , p1.getScienceScore(1));
            values.put( RTABLET     , p1.getScienceScore(2));
            values.put( RWILD      , p1.getScienceScore(3));
            values.put( RTOTAL    , p1.step_scores.get(STAGE.RESULTS));
            values.put( RPOSITION, p1.getPlace());
            values.put( RTIME, time);  
         
           // Inserting Row
            db.update(TABLE_RESULT, values, RID + " = " + p1.getRID(), null);
           
    
           id = p1.getRID();
          
          for(STAGE stage : p1.step_scores.keySet())
          {
              ContentValues score = new ContentValues();
              score.put(SSCORE, p1.step_scores.get(stage));
              if(p1.step_wins.contains(stage))
                  score.put(SMEDAL, 1);
              else
                  score.put(SMEDAL, 0);

              Log.d("updateResult", "update score table entry: RID: " + p1.getRID() + ", PID: " + p1.pid);
              db.update(TABLE_SCORE, score, 
                                  SRESULTSID + " = " + p1.getRID() 
                      + " AND " + SPLAYERID + " = " + p1.pid   
                      + " AND " + SGAMEID   + " = " + gid 
                      + " AND " + SCATEGORY + " = \"" + stage.toString() + "\""
                      , null);
               
          }
    
          db.setTransactionSuccessful();
      } finally {
          db.endTransaction();
      }
      
      
      return id;
    }
    
    


    public int removeResult(int pid, int gid) {
        Log.d("removeResult", "removeResult");
        SQLiteDatabase db = this.getWritableDatabase();

        int id = 0;
        db.beginTransaction();
        try {
            
            String query  = "SELECT " + RID + 
                    " FROM " + TABLE_RESULT + 
                    " WHERE " + RPLAYERID + " = " + pid
                    + " AND " + RGAMEID  + " = " + gid;
            Cursor cursor = db.rawQuery(query, null);
            
            int RID = -1;
            if (cursor.moveToFirst()){
                do
                {    
                    RID = cursor.getInt(cursor.getColumnIndex("RID"));
                    Log.d("removeResult", "///////////////// RID = " + RID);
                } while(cursor.moveToNext());  
            }
           
            //delete database

            Log.d("removeResult", "removing result: WHERE " + RPLAYERID + " = " + pid
                    + " AND " + RGAMEID  + " = " + gid);
            db.delete(TABLE_RESULT, RPLAYERID + " = " + pid
                                    + " AND " + RGAMEID  + " = " + gid, null);
            
            String query_scores  = "SELECT " + SCATEGORY + 
                    " FROM " + TABLE_SCORE + 
                    " WHERE " + SRESULTSID + " = " + RID + 
                    " AND " + SGAMEID   + " = " + gid +
                    " AND "  + SPLAYERID  + " = " + pid;
            Cursor cursor_scores = db.rawQuery(query_scores, null);
            
            if (cursor_scores.moveToFirst()){
                do
                {    
                    String cat = cursor_scores.getString(cursor_scores.getColumnIndex(SCATEGORY));
                    Log.d("removeResult", "removing score: " + cat);
                    db.delete(TABLE_SCORE, 
                            SRESULTSID + " = " + RID + 
                            " AND "  + SPLAYERID  + " = " + pid +
                            " AND "  + SCATEGORY  + " = \"" + cat + "\"", null);
                } while(cursor_scores.moveToNext());  
            }
    
          db.setTransactionSuccessful();
      } finally {
          db.endTransaction();
      }
      
      
      return id;
    }



    //int[] { status, pid }
    public int[] insertPlayer(String pname)
    {
        Log.d("insertPlayer", "insertPlayer");
        SQLiteDatabase db = this.getWritableDatabase();
        int[] returncode = new int[] {0,0};
         
            String query  = "SELECT " + PNAME + ", " + PID + " FROM " + TABLE_PLAYER + " WHERE " + PNAME + " = \"" + pname +"\"";
            Cursor cursor = db.rawQuery(query, null);
            
            if(cursor.getCount() != 0)
            { 
                cursor.moveToFirst();
                int exisitingPID = cursor.getInt(cursor.getColumnIndex(PID));
                
                String query2  = "SELECT " + PNAME + ", " + PID + " FROM " + TABLE_PLAYER + " WHERE " + PNAME + " = \"" + pname +"\" AND " + PDISPLAY + " = 0";
                Cursor cursor2 = db.rawQuery(query2, null);
                if(cursor2.getCount() != 0) 
                {
                    exisitingPID = cursor2.getInt(cursor2.getColumnIndex(PID));
                    return new int[] {-2, exisitingPID}; //if theres one set to not display
                }
                return new int[] {-1, exisitingPID}; //if theres one already and its displayable
            }
            int val = 0;
            if (cursor.moveToFirst()){
                do
                {    
                    val = cursor.getInt(cursor.getColumnIndex("seq"));
                } while(cursor.moveToNext());  
            }
            cursor.close(); 
        
        
//        + PID   + " INTEGER PRIMARY KEY AUTOINCREMENT ," 
//        + PNAME + " TEXT,"
//        + PDISPLAY    + " INTEGER,"
//        + PCREATEDDATE+ " TEXT" 
        ContentValues values = new ContentValues();
        values.put(PNAME, pname);  
        values.put(PDISPLAY, 1);  
        values.put(PTIME, System.currentTimeMillis());  
 
        // Inserting Row
        db.insert(TABLE_PLAYER, null, values);

        int id = getMaxID(TABLE_PLAYER);
        db.close(); // Closing database connection
        return new int[] {0, id};
    }
    
    


    
    public ArrayList<String> getPlayerNames()
    {
        SQLiteDatabase db       = this.getWritableDatabase();
        ArrayList<String> names = new ArrayList<String>();

        String query  = "SELECT " + PNAME + " FROM " + TABLE_PLAYER;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do
            {    
                names.add(cursor.getString(cursor.getColumnIndex(PNAME)));
            } while(cursor.moveToNext());  
        }
        return names;
    }
    
    public int getMaxID(String tablename){
        SQLiteDatabase db = this.getReadableDatabase();
        String query  = "SELECT seq FROM sqlite_sequence WHERE name = \"" + tablename +"\"";
        Cursor cursor = db.rawQuery(query, null);
        int val = 0;
        if (cursor.moveToFirst()){
            do
            {    
                val = cursor.getInt(cursor.getColumnIndex("seq"));
            } while(cursor.moveToNext());  
        }
        cursor.close(); 
        //Log.d("getMaxID", "VAL = " + val + " FOR " + tablename);
        return val;
    }
    
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);
        
        onCreate(db);
    }



    public void setPlayerDisplayable(int pid2, boolean b) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(b)
            values.put(PDISPLAY, 1);
        else
            values.put(PDISPLAY, 0); 
        db.update(TABLE_PLAYER, values, PID + " = " + pid2, null);
        db.close(); // Closing database connection
        
        
    }



    
    
    
    public PlayerStat getPlayerHistory(String pname, int pid)
    {
        String query  = "SELECT R.TOTAL, R.POSITION, S.GID, S.CATEGORY, S.SCORE, S.MEDAL, S.RID, G." + GEXPCODE + 
        		        " FROM PLAYER P, RESULT R, GAME G, SCORE S " +
        		        "WHERE P.PID = " + pid + " AND R.GID = G.GID AND S.RID = R.RID AND R.PID = P.PID";

//        String query  = "SELECT * FROM " + TABLE_PLAYER + " P, " 
//                                         + TABLE_RESULT + " R, " 
//                                         + TABLE_GAME   + " G, "
//                                         + TABLE_SCORE  + " S, " 
//                                         + " WHERE P.PID = " + pid + " AND R.PID = " + pid 
//                                         + " AND S.RID = R.RID AND R.PID = P.PID AND S.CATEGORY = "RESULTS"

        SQLiteDatabase db = this.getReadableDatabase();
        PlayerStat ps = new PlayerStat(pname, pid);

        Log.d("insertPlayer", "insertPlayer");
        Cursor row = db.rawQuery(query, null);
        if(row.getCount() > 0)
            if (row.moveToFirst()){
                do
                {    
                	Log.i("getPlayerHistory row stuff" , row.toString());
                    for(int i = 0; i < 6; i++)
                        Log.d("PlayerStat", "colname for " + i + " : " + row.getColumnName(i));
                    int rid      = row.getInt(row.getColumnIndex("RID")); 
                    int gid      = row.getInt(row.getColumnIndex("GID")); 
                   
                    int position = row.getInt(row.getColumnIndex("POSITION")); 
                    int total    = row.getInt(row.getColumnIndex("TOTAL"));  
                    int score    = row.getInt(row.getColumnIndex("SCORE"));
                    int medal    = row.getInt(row.getColumnIndex("MEDAL")); 
                    String cat   = row.getString(row.getColumnIndex("CATEGORY"));
                    
                    ResultStat gs;
                    
                    //if game is already listed
                    if(ps.game_list.containsKey(gid))
                    {
                        gs = ps.game_list.get(gid);
                    }
                    //if game is not already in playerstat
                    else
                    {
                        gs = new ResultStat();
                        ps.game_list.put(gid, gs);
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
                } while(row.moveToNext());  
            } 
        
        return ps;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    public HashMap<String, Integer> getPlayerNamePIDs()
    {
        SQLiteDatabase db       = this.getReadableDatabase(); 
        HashMap<String, Integer> names = new HashMap<String, Integer>();

        String query  = "SELECT " + PNAME + ", " + PID + " FROM " + TABLE_PLAYER;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do
            {    
                names.put( cursor.getString(cursor.getColumnIndex(PNAME)), cursor.getInt(cursor.getColumnIndex(PID))); 
            } while(cursor.moveToNext());  
        }
        return names;
    }
    

}
