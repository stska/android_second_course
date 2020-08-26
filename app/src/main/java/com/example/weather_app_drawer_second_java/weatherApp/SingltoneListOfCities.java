package com.example.weather_app_drawer_second_java.weatherApp;

import android.content.res.Resources;

import com.example.weather_app_drawer_second_java.R;
import com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass.Example2;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;

public class SingltoneListOfCities {
    private static SingltoneListOfCities instance;
    public Example2[] example2;
    private Resources res;

    private SingltoneListOfCities(Resources res){
        this.res = res;

            Writer writer = new StringWriter();

           // BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.city)));
        BufferedReader reader = new BufferedReader(new InputStreamReader(res.openRawResource(R.raw.city)));

            try {
                String line = reader.readLine();
                while (line != null) {
                    writer.write(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.example2 =  new Gson().fromJson(writer.toString(),Example2[].class);

    }
    public static synchronized SingltoneListOfCities getInstance(Resources res){
        if(instance == null){

            instance = new SingltoneListOfCities(res);
        }
        return instance;
    }
}
