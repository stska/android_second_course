package com.example.weather_app_drawer_second_java.weatherApp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {WeatherEntity.class}, version = 1, exportSchema = false)
public abstract class WeatherDatabaseRoom extends RoomDatabase {
    public abstract WeatherDaoInterface getWeather();
}
