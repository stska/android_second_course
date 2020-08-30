package com.example.weather_app_drawer_second_java.weatherApp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SharedPreferencesClass {
    SharedPreferences sharedPreferences;

    public static SharedPreferences getPrefs(Context context){
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public static void insertData(Context context,String key,String value){
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static String getData(Context context,String key){
        return getPrefs(context).getString(key,"opppss, there is no data found");
    }
    public static Set<String> getStringsSEt(Context context,String key){
        return getPrefs(context).getStringSet(key,null);
    }
    public static void deleteData(Context context,String key){
        SharedPreferences.Editor editor = getPrefs(context).edit();
         editor.remove(key);
         editor.commit();
    }
}
