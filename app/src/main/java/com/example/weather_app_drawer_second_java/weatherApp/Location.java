package com.example.weather_app_drawer_second_java.weatherApp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.weather_app_drawer_second_java.OpenWeatherAPI;
import com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass.WeatherParsing;
import com.google.android.gms.maps.model.LatLng;

import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Location {
    Context context;
    Activity activity;

    public Location(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }


    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        // LocationManager locationManager = null;
        // if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        //     locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        // }


        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 10000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull android.location.Location location) {
                    double lat = location.getLatitude();
                    // String latitude = Double.toString(lat);
                  String  latitude = Double.toString(lat);

                    double lng = location.getLongitude();
                   String  longitude = Double.toString(lng);
                    //   String longitude = Double.toString(lng);
                    //String accuracy = Float.toString(location.getAccuracy());

                    LatLng currentPosition = new LatLng(lat, lng);


              /*      new Thread(new Runnable() {
                        @Override
                        public void run() {
                           // retrofitCurrentPositionRequest.requestRetrofit(currentPosition);
                            retrofitCurrentPositionRequest.requestRetrofit(latitude,longitude);

                            try{
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String tmp =  retrofitCurrentPositionRequest.getCityTmp();
                                        dayTmp.setText(tmp);
                                        dayTmp.setVisibility(View.VISIBLE);
                                        String city = retrofitCurrentPositionRequest.getCityName();
                                        cityInitTxt.setText(city);
                                        cityInitTxt.setVisibility(View.VISIBLE);
                                        String description = retrofitCurrentPositionRequest.getWeatherDscrp();
                                        waetherDescr.setText(description);
                                        waetherDescr.setVisibility(View.VISIBLE);
                                    }
                                });
                            } catch (Exception ignored) {}
                        }
                    }).start();  */

                    //-----------------------------------------
                }

            });







        }

    }
    private void requestPemissions(){
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            requestLocation();
            //setCurrentCityWeather(latitude,longitude);


        }else {
            requestLocationPermission();
        }
    }
    private void requestLocationPermission(){
        if(!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 10);
        }
    }




}
