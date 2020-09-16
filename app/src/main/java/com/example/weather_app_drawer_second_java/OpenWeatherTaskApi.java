package com.example.weather_app_drawer_second_java;

import com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass.WeatherParsing;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface OpenWeatherTaskApi {
    @GET("data/2.5/weather")
    Call<WeatherParsing> loadData(@QueryMap Map<String,String> currentPosition,
                                  @Query("units") String units,
                                  @Query("appid") String keyApi);
}
