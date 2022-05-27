package com.example.javanoteapp.data.repository;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import com.example.javanoteapp.MyApplication;
import com.example.javanoteapp.constants.NoteFilter;


public class AppPreferences {
    private static AppPreferences instance;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFERENCE = "app_shared_preferences";
    private static final String FILTER_KEY = "filter_preference";
    private AppPreferences(){}

    public static AppPreferences getInstance(){
        if(instance == null){
            instance = new AppPreferences();
        }
        return instance;
    }

    private SharedPreferences getPreference(){
        if(sharedPreferences == null){
            sharedPreferences = MyApplication.getAppContext().getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public void setFilter(NoteFilter filter){
        SharedPreferences sharedPreferences = getPreference();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FILTER_KEY, filter.name());
        editor.apply();
    }

    public NoteFilter getFilter(){
        SharedPreferences sharedPreferences = getPreference();
        String filterString = sharedPreferences.getString(FILTER_KEY, NoteFilter.none.name());
        return NoteFilter.valueOf(filterString);
    }
}
