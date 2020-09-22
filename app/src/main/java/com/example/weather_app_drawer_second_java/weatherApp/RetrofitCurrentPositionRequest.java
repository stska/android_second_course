package com.example.weather_app_drawer_second_java.weatherApp;

import android.content.Context;
import android.widget.ProgressBar;

import com.example.weather_app_drawer_second_java.OpenWeatherAPI;
import com.example.weather_app_drawer_second_java.OpenWeatherTaskApi;
import com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass.WeatherParsing;
import com.example.weather_app_drawer_second_java.weatherApp.database.WeatherEntity;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCurrentPositionRequest {
    private final String METRIC = "metric";
    private final String IMPERIAL = "";
    private final String weatherSite = "https://api.openweathermap.org";
    private final String apiKey = "80b8b51878e4ae64fc72d800c1679d04";
    private final String UNITS = "units";
    private final String CELCSIUS = "celsius";
    private OpenWeatherTaskApi OpenWeatherTaskApi;
    private String cityTmp;
    private String weatherDscrp;
    private Context context;
    private String cityName;

    public RetrofitCurrentPositionRequest(Context context){
        this.context = context;
        initRetrofit();
    }

    public void requestRetrofit(String lat, String lon){
         Map<String,String> latLon = new HashMap<>();
        latLon.put("lat",lat);
         latLon.put("lon",lon);

        OpenWeatherTaskApi.loadData(latLon,UNITS,apiKey).enqueue(new Callback<WeatherParsing>() {
            @Override
            public void onResponse(Call<WeatherParsing> call, Response<WeatherParsing> response) {
                if(response.body() != null){
                    weatherDscrp = response.body().getWeather().get(0).getDescription();
                    cityName = response.body().getName();
                    cityTmp = response.body().getMain().getTemp().toString();
                }
            }

            @Override
            public void onFailure(Call<WeatherParsing> call, Throwable t) {
                //TODO ERROR
            }
        });
    }
    private void initRetrofit(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(weatherSite).addConverterFactory(GsonConverterFactory.create()).build();
        OpenWeatherTaskApi = retrofit.create(OpenWeatherTaskApi.class);
    }

    public String getCityTmp() {
        return cityTmp;
    }
    public String getCityName(){
        return cityName;
    }
    public String getWeatherDscrp() {
        return weatherDscrp;
    }
}
