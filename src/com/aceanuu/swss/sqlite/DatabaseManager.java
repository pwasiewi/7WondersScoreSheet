package com.aceanuu.swss.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


        @SuppressWarnings("unused")
public class DatabaseManager extends SQLiteOpenHelper  {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "SEVEN_WONDERS_SCORE_SHEET_DATABASE";
 
    // Contacts table name
    private static final String TABLE_PLAYER = "PLAYER";
        private static final String PID    = "PID";
        private static final String PNAME  = "NAME";
        private static final String PDISPLAY     = "DISPLAY";
        private static final String PCREATEDDATE = "CREATEDDATE";

    private static final String EXPANSIONSCORESJUNCTION = "EXPANSIONSCORESJUNCTION";
        private static final String ESJRESULTID          = "RESULTID";
        private static final String ESJEXPANSIONSCOREID  = "EXPANSIONSCOREID"; 

    private static final String LEADERSSCORE = "LEADERSSCORE";
        private static final String LSLEADERSID    = "LEADERSID";
        private static final String LSSCORE      = "LEADERSSCORE"; 
        private static final String LSMEDAL      = "LEADERSMEDAL"; 

    private static final String CITIESSSCORE = "LEADERSSCORE";
        private static final String CSCITIESID    = "CITIESID";
        private static final String CSCITIESSCORE      = "CITIESSCORE"; 
        private static final String CSCITIESMEDAL      = "CITIESMEDAL";
        private static final String CSDEBTSCORE      = "DEBTSCORE"; 
        private static final String CSDEBTMEDAL      = "DEBTMEDAL"; 
    
    private static final String RESULTS    = "RESULTS";
        private static final String RRRESULTSID  = "RID";
        private static final String RPLAYERID    = "PID";
        private static final String REMPIRENAME  = "EMPIRENAME";
        private static final String REMPIRESLOTS = "EMPIRESLOTS";
        private static final String REMPIRESLOTSFILLED = "EMPIRESLOTSFILLED";
        private static final String RMILITARY       = "MILITARY";
        private static final String RMILITARYMEDAL  = "MILITARYMEDAL";
        private static final String RMONEY       = "MONEY";
        private static final String RMONEYMEDAL  = "MONEYMEDAL";
        private static final String REMPIRE      = "EMPIRE";
        private static final String REMPIREMEDAL = "EMPIREMEDAL";
        private static final String RCIVILIAN      = "CIVILIAN";
        private static final String RCIVILIANMEDAL  = "CIVILIANMEDAL";
        private static final String RCOMMERCIAL       = "COMMERCIAL";
        private static final String RCOMMERCIALMEDAL  = "COMMERCIALMEDAL";
        private static final String RSCIENCE   = "SCIENCE";
        private static final String RSCIENCEMEDAL  = "SCIENCEMEDAL";
        private static final String RGUILD       = "GUILD";
        private static final String RGUILDMEDAL  = "GUILDMEDAL";
        private static final String RTOTAL       = "TOTAL";
        private static final String RPOSITION    = "POSITION";   
     
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); 
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLAYER_TABLE = "CREATE TABLE " + TABLE_PLAYER 
                + "("
                + PID   + " INTEGER PRIMARY KEY AUTOINCREMENT ," 
                + PNAME + " TEXT,"
                + PDISPLAY    + " INTEGER,"
                + PCREATEDDATE+ " TEXT" 
                + ")";
        db.execSQL(CREATE_PLAYER_TABLE);
        

        String CREATE_PLAYER_TABLE = "CREATE TABLE " + TABLE_PLAYER 
                + "("
                + PID   + " INTEGER PRIMARY KEY AUTOINCREMENT ," 
                + PNAME + " TEXT,"
                + PDISPLAY    + " INTEGER,"
                + PCREATEDDATE+ " TEXT" 
                + ")";
        db.execSQL(CREATE_PLAYER_TABLE);
        
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        
    }

}
