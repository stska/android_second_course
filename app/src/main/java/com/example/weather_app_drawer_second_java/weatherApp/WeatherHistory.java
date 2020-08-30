package com.example.weather_app_drawer_second_java.weatherApp;

import java.util.ArrayList;
import java.util.List;

public class WeatherHistory {

    /*
    cityName = data.getName();
        cityTmp = data.getMain().getTemp().toString();
        cityHum = data.getMain().getHumidity().toString();
        cityWind = data.getWind().getSpeed().toString();
        cityPres = data.getMain().getPressure().toString();

        -----------
        private TextView textView;
          private TextView weatherText;
          private TextView pressureText;
          private TextView tempText;
          private TextView humidityText;
     */
    private String cityName;
    private String  cityTmp;
    private String cityPressure;
    private String weatherText;
    private boolean favFlag;

    public static ArrayList<WeatherHistory> weatherHistories =  new ArrayList<>();

    public WeatherHistory(String cityName,String cityTmp, String cityPressure,  String weatherText){
        this.cityName = cityName;
        this.cityTmp = cityTmp;
        this.cityPressure = cityPressure;
        this.weatherText = weatherText;
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
    public boolean getFavFlag() { return favFlag; }
    public void setFavFlag(boolean favFlag){
         this.favFlag = favFlag;
    }
}
