package com.example.weather_app_drawer_second_java;

import com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass.WeatherParsing;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherAPI {
    @GET("data/2.5/weather")
    Call<WeatherParsing> loadData(@Query("q") String city,
                                  @Query("units") String units,
                                  @Query("appid") String keyApi);
}


