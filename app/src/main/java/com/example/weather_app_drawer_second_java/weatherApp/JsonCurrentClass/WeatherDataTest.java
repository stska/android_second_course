package com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass;


import com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass.Main;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherDataTest {

    @SerializedName("main")
    @Expose
    private Main main;
    public Main getMain(){
        return main;
    }
    public void setMain(Main main){
        this.main = main;
    }
}
