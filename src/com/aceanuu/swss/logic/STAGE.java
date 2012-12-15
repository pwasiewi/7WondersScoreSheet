package com.aceanuu.swss.logic;

import android.util.Log;

public enum STAGE {
    PLAYERS, MILITARY, MONEY, DEBT, WONDER, CIVILIAN, COMMERCIAL, SCIENCE, GUILD,  LEADERS,  CITIES, RESULTS;
    
    public static String toString(STAGE _enum)
    {
        String result = null;
        switch (_enum) {
            case PLAYERS:
                result = "Players";
                break;
            case MILITARY:
                result = "Military";
                break;
            case MONEY:
                result = "Money";
                break;
            case WONDER:
                result = "Wonder";
                break;
            case CIVILIAN:
                result = "Civilian";
                break;
            case COMMERCIAL:
                result = "Commercial";
                break;
            case GUILD:
                result = "Guilds";
                break;
            case SCIENCE:
                result = "Science";
                break;
            case LEADERS:
                result = "Leaders";
                break;
            case DEBT:
                result = "Debt";
                break;
            case CITIES:
                result = "Cities";
                break;
            case RESULTS:
                result = "Results";
                break;
        }
        if(result == null)
            throw new IllegalArgumentException();
        return result;
    }
    
    public static String toLabel(STAGE _enum)
    {
        String result = null;
        switch (_enum) {
            case PLAYERS:
                result = "Wonder";
                break;
            case MILITARY:
                result = "Points";
                break;
            case MONEY:
                result = "Points";
                break;
            case WONDER:
                result = "Points";
                break;
            case CIVILIAN:
                result = "Points";
                break;
            case COMMERCIAL:
                result = "Points";
                break;
            case GUILD:
                result = "Points";
                break;
            case SCIENCE:
                result = "Points";
                break;
            case LEADERS:
                result = "Points";
                break;
            case DEBT:
                result = "Points";
                break;
            case CITIES:
                result = "Points";
                break;
            case RESULTS:
                result = "Results";
                break;
        }
        if(result == null)
            throw new IllegalArgumentException();
        return result;
    }
    

    public static STAGE convertStringToEnum(String enum_text)
    {
        Log.d("convertStringToEnum", enum_text);
        enum_text = enum_text.toUpperCase(); 
        if(enum_text.equals("PLAYERS")) 
            return STAGE.PLAYERS; 
        if(enum_text.equals("MILITARY")) 
            return STAGE.MILITARY; 
        if(enum_text.equals("MONEY")) 
            return STAGE.MONEY; 
        if(enum_text.equals("WONDER")) 
            return STAGE.WONDER; 
        if(enum_text.equals("CIVILIAN")) 
            return STAGE.CIVILIAN;
        if(enum_text.equals("COMMERCIAL")) 
            return STAGE.COMMERCIAL;
        if(enum_text.equals("GUILD") || enum_text.equals("GUILDS")) 
            return STAGE.GUILD;
        if(enum_text.equals("SCIENCE")) 
            return STAGE.SCIENCE;
        if(enum_text.equals("LEADERS")) 
            return STAGE.LEADERS;
        if(enum_text.equals("DEBT")) 
            return STAGE.DEBT;
        if(enum_text.equals("CITIES")) 
            return STAGE.CITIES;
        if(enum_text.equals("RESULTS")) 
            return STAGE.RESULTS;

        throw new IllegalArgumentException();
    }
}
