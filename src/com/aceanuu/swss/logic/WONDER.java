package com.aceanuu.swss.logic;

public enum WONDER {
    Alexandria, Babylon, Byzantium, Catan, Ephesos, Giza, Halikarnassos, MannekenPis, Olympia, Petra, Rhodos, Rome;
    
    
    public static String toString(WONDER _enum) {
        String result = ""; 
        switch (_enum) {
            case Babylon:
                result = "Babylon";
                break;
            case Rhodos:
                result = "Rhodos";
                break;
            case Alexandria:
                result = "Alexandria";
                break;
            case Ephesos:
                result = "Ephesos";
                break;
            case Olympia:
                result = "Olympia";
                break;
            case Giza:
                result = "Giza";
                break;
            case Halikarnassos:
                result = "Halikarnassos";
                break;
            case MannekenPis:
                result = "Manneken Pis";
                break;
            case Catan:
                result = "Catan";
                break;
            case Petra:
                result = "Petra";
                break;
            case Byzantium:
                result = "Byzantium";
                break;
            case Rome:
                result = "Rome";
                break;
        }
        if(result == "")
            throw new IllegalArgumentException();
        
        return result;
    }


}
