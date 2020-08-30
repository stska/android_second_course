package com.example.weather_app_drawer_second_java.weatherApp;

import java.util.ArrayList;

public class FavouriteCities {

    private String cityName;
    private String  cityTmp;
    private String cityPressure;
    private String weatherText;

    public static ArrayList<FavouriteCities> favCities =  new ArrayList<>();

    public FavouriteCities(String cityName,String cityTmp, String cityPressure,  String weatherText){
        this.cityName = cityName;
        this.cityTmp = cityTmp;
        this.cityPressure = cityPressure;
        this.weatherText = weatherText;
        favCities.add(this);
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityPressure() {
        return cityPressure;
    }

    public String getCityTmp() {
        return cityTmp;
    }

    public String getWeatherText() {
        return weatherText;
    }

}
