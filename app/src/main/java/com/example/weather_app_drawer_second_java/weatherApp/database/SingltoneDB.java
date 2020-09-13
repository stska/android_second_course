package com.example.weather_app_drawer_second_java.weatherApp.database;

import android.content.Context;

import androidx.room.Room;

public class SingltoneDB {

    private static SingltoneDB instance;
    private WeatherDatabaseRoom db;

    private SingltoneDB(Context context) {
        db = Room.databaseBuilder(context, WeatherDatabaseRoom.class, "weather").allowMainThreadQueries().build();
    }

    public static SingltoneDB getInstance(Context context) {
        if (instance == null) {
            instance = new SingltoneDB(context);
        }
        return instance;
    }

    public WeatherDaoInterface getDb() {
        return db.getWeather();
    }

}
