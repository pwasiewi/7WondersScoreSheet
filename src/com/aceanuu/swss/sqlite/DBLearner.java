package com.aceanuu.swss.sqlite;

import com.example.sqlitetester.Player;
import com.example.sqlitetester.STAGE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



//select a game from a GID
//SELECT * FROM PLAYER P, RESULT R, GAME G, SCORE S WHERE G.GID = 3 AND R.GID = G.GID AND S.RID = R.RID AND R.PID = P.PID

//average total score from a PID
//SELECT avg(S.SCORE) FROM PLAYER P, RESULT R, GAME G, SCORE S WHERE P.PID = 1 AND R.GID = G.GID AND S.RID = R.RID AND R.PID = P.PID AND S.CATEGORY = "RESULTS"

//wonder that is the highest scoring in science
//SELECT  R.EMPIRENAME FROM PLAYER P, RESULT R, GAME G, SCORE S
//WHERE R.GID = G.GID AND S.RID = R.RID AND R.PID = P.PID AND S.CATEGORY = "SCIENCE" ORDER BY S.SCORE DESC LIMIT 1

public class DBLearner extends SQLiteOpenHelper  {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 5;
 
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
     
    public DBLearner(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); 
        //Log.d("DatabaseManager", "CREATING DatabaseManager");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.d("onCreate", "CREATING TABLES AND SHIT");
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
                + SSCOREID   + " INTEGER PRIMARY KEY AUTOINCREMENT," 
                + SRESULTSID    +  " INTEGER,"
                + SPLAYERID     +  " INTEGER,"
                + SCATEGORY    + " TEXT,"
                + SSCORE    + " INTEGER,"
                + SMEDAL    + " INTEGER,"
                + "FOREIGN KEY(" + SRESULTSID + ") REFERENCES " + TABLE_RESULT + "(" + RID + ")" 
                + "FOREIGN KEY(" + SPLAYERID + ") REFERENCES " + TABLE_PLAYER + "(" + PID + ")" 
                + ");";
        db.execSQL(CREATE_TABLE_SCORE);
        

        String CREATE_TABLE_GAME = "CREATE TABLE " + TABLE_GAME 
                + "("
                + GID   + " INTEGER PRIMARY KEY AUTOINCREMENT," 
                + GTIME    + " INTEGER,"
                + GPCOUNT    + " INTEGER,"
                + GEXPCODE    + " TEXT"
                + ");";
        db.execSQL(CREATE_TABLE_GAME);
    }

    
    
    
    
    
    public int insertGame(int pcount)
    {
        SQLiteDatabase db = this.getWritableDatabase();

//        + GID   + " INTEGER PRIMARY KEY AUTOINCREMENT," 
//        + GTIME    + " TEXT,"
//        + GPCOUNT    + " INTEGER,"
//        + GEXPCODE    + " TEXT" 
      ContentValues values = new ContentValues();
      values.put(GEXPCODE, "");  
      values.put(GPCOUNT, pcount);  
      values.put(GTIME, System.currentTimeMillis());  

      // Inserting Row
      db.insert(TABLE_GAME, null, values);

      int id = getMaxID(TABLE_GAME);
      db.close(); // Closing database connection
      return id;
        
    }
    

    public int insertPlayer(String pname)
    {
        SQLiteDatabase db = this.getWritableDatabase();

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
        return id;
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


    public int insertResult(Player p1, int gid) {
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
      
      db.close(); // Closing database connection
      
      return id;
    }
    

}
