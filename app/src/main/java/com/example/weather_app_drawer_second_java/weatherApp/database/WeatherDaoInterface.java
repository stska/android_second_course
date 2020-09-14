package com.example.weather_app_drawer_second_java.weatherApp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WeatherDaoInterface {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeather(WeatherEntity weatherEntity);

    @Update
    void updateWeather(WeatherEntity weatherEntity);

    @Delete
    void deleteWeather(WeatherEntity weatherEntity);

    @Query("DELETE FROM weather WHERE id = :id")
    void deleteWeatherById(long id);

    @Query("SELECT * FROM weather")
    List<WeatherEntity> getAllWeather();

    @Query("SELECT * FROM weather WHERE id = :id")
    WeatherEntity getWeatherById(long id);

    @Query("SELECT COUNT() FROM weather")
    long getCountWeather();

   @Query("SELECT favourite  FROM weather WHERE id = :id")
    boolean getFavourite(long id);
   @Query("UPDATE WEATHER SET favourite = :newFavourite WHERE id = :id")
    void updateFavourite(long id,boolean newFavourite);
}
