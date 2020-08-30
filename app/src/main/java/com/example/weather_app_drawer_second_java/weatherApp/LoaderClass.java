package com.example.weather_app_drawer_second_java.weatherApp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass.Example;
import com.example.weather_app_drawer_second_java.weatherApp.JsonForecastClasses.Example3;
import com.example.weather_app_drawer_second_java.weatherApp.JsonForecastClasses.Main;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
//https://openweathermap.org/current


public class LoaderClass extends AsyncTaskLoader<Example> {

    private String mParam2;
    private ProgressBar progressBar;
    private final String METRIC = "&units=metric";
    private final String IMPERIAL = "";
    private final String weatherSite = "https://api.openweathermap.org/data/2.5/weather?q=";
    private final String apiKey = "&appid=80b8b51878e4ae64fc72d800c1679d04";
    private final String UNITS = "units";
    private final String CELCSIUS = "celsius";

    public LoaderClass(@NonNull Context context, String data, ProgressBar progressBar) {
        super(context);
        this.mParam2 = data;
        this.progressBar = progressBar;
    }

    @Nullable
    @Override
    public Example loadInBackground() {
        String units = SharedPreferencesClass.getData(getContext(),UNITS).contains(CELCSIUS) ? METRIC : IMPERIAL;
        String url2 = weatherSite.concat(mParam2).concat(units).concat(apiKey);

        try {
            final URL url = new URL(url2);
            HttpsURLConnection urlConnection = null;
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);

            int code = urlConnection.getResponseCode();
            if (code >= 200 && code <= 299) {

                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String result = null;
                if ((result = in.readLine()) != null) {

                }
                Example data = new Gson().fromJson(result, Example.class);

                String url3 = "https://api.openweathermap.org/data/2.5/forecast?q=".concat(mParam2).concat("&appid=80b8b51878e4ae64fc72d800c1679d04");
                try {
                    final URL url4 = new URL(url3);

                    HttpsURLConnection urlConnection2 = null;
                    urlConnection2 = (HttpsURLConnection) url4.openConnection();
                    urlConnection2.setRequestMethod("GET");
                    urlConnection2.setReadTimeout(10000);
                    int code2 = urlConnection2.getResponseCode();
                    if (code2 >= 200 && code2 <= 299) {
                        BufferedReader inn = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream()));
                        String result2 = null;

                        if ((result2 = inn.readLine()) != null) {

                        }
                        Example3 data2 = new Gson().fromJson(result2,  Example3.class);
                        System.out.println(data2.getList().size());

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
             return data;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
