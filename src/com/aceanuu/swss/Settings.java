package com.aceanuu.swss;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Settings extends PreferenceActivity {

    private final static String PREF_KEY = "seven_wonders_score_sheet_preferences";
    private SharedPreferences prefs;
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		getPreferenceManager().setSharedPreferencesName(PREF_KEY); 
		addPreferencesFromResource(R.xml.preferences);
		prefs = getSharedPreferences(PREF_KEY, MODE_PRIVATE); 
//		
//		final EditTextPreference playerNamePref = (EditTextPreference) findPreference("player_name");
//		
//		
//		String playerName = prefs.getString("player_name",  
//				getResources().getString(R.string.player_name)); 
//		
//		
//		
//		playerNamePref.setSummary((CharSequence) playerName);  

        final String science_key = getApplicationContext().getResources().getString(R.string.settings_expanded_science_key);
        final String leaders_key = getApplicationContext().getResources().getString(R.string.settings_expansions_leaders_key);
        final String cities_key  = getApplicationContext().getResources().getString(R.string.settings_expansions_cities_key);
        

        final Preference sciencesetting = findPreference(science_key);
        sciencesetting.setOnPreferenceChangeListener(new OnPreferenceChangeListener() { 
            public boolean onPreferenceChange(Preference preference, Object newValue) { 
                SharedPreferences.Editor ed = prefs.edit(); 
                ed.putBoolean(science_key, ((Boolean) newValue)); 
                ed.commit();     
                return true; 
            } 
        });

        final Preference leaderssetting = findPreference(leaders_key);
        leaderssetting.setOnPreferenceChangeListener(new OnPreferenceChangeListener() { 
            public boolean onPreferenceChange(Preference preference, Object newValue) { 
                SharedPreferences.Editor ed = prefs.edit(); 
                ed.putBoolean(leaders_key, ((Boolean) newValue)); 
                ed.commit();     
                return true; 
            } 
        });
        
        final Preference citiessetting = findPreference(cities_key);
        citiessetting.setOnPreferenceChangeListener(new OnPreferenceChangeListener() { 
            public boolean onPreferenceChange(Preference preference, Object newValue) { 
                SharedPreferences.Editor ed = prefs.edit(); 
                ed.putBoolean(cities_key, ((Boolean) newValue)); 
                ed.commit();     
                return true; 
            } 
        });
	}

}