package com.aceanuu.swss.logic;


public class EnumConverter {

    public static String stages(Stages _enum) {
        String result = "";
        
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
        if(result == "")
            throw new IllegalArgumentException();
        
        return result;
    }
//
//    public static String wonders(Wonders _enum) {
//        String result = ""; 
//        switch (_enum) {
//            case BABYLON:
//                result = "Babylon";
//                break;
//            case RHODOS:
//                result = "Rhodos";
//                break;
//            case ALEXANDRIA:
//                result = "Alexandria";
//                break;
//            case EPHOSOS:
//                result = "Ephosos";
//                break;
//            case OLYMPIA:
//                result = "Olympia";
//                break;
//            case GIZA:
//                result = "Giza";
//                break;
//            case HALIKARNASSOS:
//                result = "Halikarnassos";
//                break;
//            case MANNEKEN_PIS:
//                result = "Manneken_pis";
//                break;
//            case CATAN:
//                result = "Catan";
//                break;
//            case PETRA:
//                result = "Petra";
//                break;
//            case BYZANTIUM:
//                result = "Byzantium";
//                break;
//        }
//        if(result == "")
//            throw new IllegalArgumentException();
//        
//        return result;
//    }
}
