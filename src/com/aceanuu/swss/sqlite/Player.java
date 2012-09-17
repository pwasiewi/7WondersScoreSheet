package com.aceanuu.swss.sqlite;

import java.util.Map;
import java.util.TreeMap;

import com.aceanuu.swss.Stages;
import com.aceanuu.swss.Wonders;


public class Player {

    private String               name;
    private Wonders              wonder;
    private Map<Stages, Integer> steps;

    public Player(String _name, Wonders _wonder) {
        name = _name;
        wonder = _wonder;
        steps = new TreeMap<Stages, Integer>();
    }

    public String getName() {
        return name;
    }

    public Wonders getWonder() {
        return wonder;
    }

    public boolean resetScores() {
        steps = new TreeMap<Stages, Integer>();
        return true;
    }
}
