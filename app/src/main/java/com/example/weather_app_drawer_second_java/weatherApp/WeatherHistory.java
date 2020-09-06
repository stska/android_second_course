package com.example.weather_app_drawer_second_java.weatherApp;

import java.util.ArrayList;

public class WeatherHistory {

    private String cityName;
    private String cityTmp;
    private String cityPressure;
    private String weatherText;
    private String humText;
    private boolean favFlag;
    private String icon;

    public static ArrayList<WeatherHistory> weatherHistories = new ArrayList<>();

    public WeatherHistory(String cityName, String cityTmp, String cityPressure, String weatherText, String humText,String icon) {
        this.cityName = cityName;
        this.cityTmp = cityTmp;
        this.cityPressure = cityPressure;
        this.weatherText = weatherText;
        this.humText = humText;
        this.icon = icon;
        favFlag = false;
        weatherHistories.add(this);
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

    public boolean getFavFlag() {
        return favFlag;
    }

    public void setFavFlag(boolean favFlag) {
        this.favFlag = favFlag;
    }

    public String getHumText() {
        return humText;
    }
    public String getIcon() { return icon;}
}
