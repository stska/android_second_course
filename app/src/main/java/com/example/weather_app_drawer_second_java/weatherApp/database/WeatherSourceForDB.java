package com.example.weather_app_drawer_second_java.weatherApp.database;

import java.util.List;

public class WeatherSourceForDB {
    private List<WeatherEntity> weatherEntityList;
    public final WeatherDaoInterface weatherDaoInterface;
    public WeatherSourceForDB(WeatherDaoInterface weatherDaoInterface){
        this.weatherDaoInterface = weatherDaoInterface;
    }
    public List<WeatherEntity> getWeatherEntityList(){
        if(weatherEntityList == null){
            LoadWeatherData();
        }
        return weatherEntityList;
    }
    public void LoadWeatherData(){
        weatherEntityList = weatherDaoInterface.getAllWeather();
    }
    public void addWeather(WeatherEntity weatherEntity){
        weatherDaoInterface.insertWeather(weatherEntity);
        LoadWeatherData();
    }
    public long getCountWeather(){
        return weatherDaoInterface.getCountWeather();
    }

    public void updateWeather(WeatherEntity weatherEntity){
        weatherDaoInterface.updateWeather(weatherEntity);
        LoadWeatherData();
    }
    public void removeWeather(long id){
        weatherDaoInterface.deleteWeatherById(id);
        LoadWeatherData();
    }
    public void deleteWeatherLikeObject(WeatherEntity weatherEntity){
        weatherDaoInterface.deleteWeather(weatherEntity);
        LoadWeatherData();
    }
    public void getFavourite(long id){
        weatherDaoInterface.getFavourite(id);
    }
    public void updateFavourite(long id,boolean favourite){
        weatherDaoInterface.updateFavourite(id, favourite);
        LoadWeatherData();
    }

}
