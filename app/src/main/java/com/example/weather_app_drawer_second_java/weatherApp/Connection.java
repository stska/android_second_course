package com.example.weather_app_drawer_second_java.weatherApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Connection implements Runnable {
    HttpsURLConnection urlConnection = null;
    String apiKey = "80b8b51878e4ae64fc72d800c1679d04";
    URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?id=524901&APPID={80b8b51878e4ae64fc72d800c1679d04}");
    boolean flag = true;

    public Connection() throws IOException {

    }




    @Override
    public void run() {
        //while(flag){
          //  try{urlConnection.setRequestMethod("GET").
            //    urlConnection.setRequestMethod("GET");
              //  urlConnection.setConnectTimeout(10000);
            //}catch (IOException e){
              //  e.addSuppressed();
            //}
        //}

    }
}
