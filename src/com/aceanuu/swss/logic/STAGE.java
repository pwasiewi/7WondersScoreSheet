package com.aceanuu.swss.logic;

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
            case RESULTS:
                result = "Results";
                break;
        }
        if(result == null)
            throw new IllegalArgumentException();
        return result;
    }
}
