package com.example.weather_app_drawer_second_java.weatherApp;

import android.content.res.Resources;
import android.widget.Toast;

import com.example.weather_app_drawer_second_java.MainActivity;
import com.example.weather_app_drawer_second_java.R;
import com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass.Example2;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

public class SingltoneListOfCities {
    private static SingltoneListOfCities instance;
    public Example2[] example2;
    private Resources res;
    private ArrayList<String> listOfCities;

    private SingltoneListOfCities(Resources res) throws IOException {
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
            this.listOfCities = fullCitiesList();

    }
    private ArrayList<String> fullCitiesList() throws IOException {
         ArrayList<String> tmp = new ArrayList<>();

        for(int i = 0;i < example2.length;i++){
           tmp.add(example2[i].getName());
        }
        return tmp;
    }
    public static synchronized SingltoneListOfCities getInstance(Resources res) throws IOException {
        if(instance == null){

            instance = new SingltoneListOfCities(res);
        }
        return instance;
    }

    public ArrayList<String> getListOfCities() {
        return listOfCities;
    }
}
