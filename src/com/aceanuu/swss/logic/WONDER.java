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

    
    public static WONDER convertStringToEnum(String enum_text) {
        enum_text = enum_text.toUpperCase();
        if(enum_text.equals("BABYLON"))
            return WONDER.Babylon;
        if(enum_text.equals("RHODOS"))
            return WONDER.Rhodos;
        if(enum_text.equals("ALEXANDRIA"))
            return WONDER.Alexandria;
        if(enum_text.equals("EPHESOS"))
            return WONDER.Ephesos;
        if(enum_text.equals("OLYMPIA"))
            return WONDER.Olympia;
        if(enum_text.equals("GIZA"))
            return WONDER.Giza;
        if(enum_text.equals("HALIKARNASSOS"))
            return WONDER.Halikarnassos;
        if(enum_text.equals("CATAN"))
            return WONDER.Catan;
        if(enum_text.equals("PETRA"))
            return WONDER.Petra;
        if(enum_text.equals("MANNEKEN PIS"))
            return WONDER.MannekenPis;
        if(enum_text.equals("BYZANTIUM"))
            return WONDER.Byzantium;
        if(enum_text.equals("ROME"))
            return WONDER.Rome; 
        throw new IllegalArgumentException();
    }

}
