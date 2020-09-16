package com.example.weather_app_drawer_second_java.weatherApp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_app_drawer_second_java.OpenWeatherAPI;
import com.example.weather_app_drawer_second_java.OpenWeatherTaskApi;
import com.example.weather_app_drawer_second_java.R;
import com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass.WeatherParsing;
import com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass.WeatherParsingVersionTwo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.security.Policy;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class InitialFragment extends Fragment implements PropertyChangeListener, OnMapReadyCallback {
    OnItemClickedListener mListener;
    private static String flat = "";
    private WeatherParsingVersionTwo[] example2s;
    private CardView cardView;
    private TextView dayTmp;
    private TextView waetherDescr;
    private TextView cityInitTxt;
    private FragmentActivity myContext;
    private SingltoneListOfCities singltoneListOfCities;
    private TextView favTitle;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private final String METRIC = "metric";
    private final String IMPERIAL = "";
    private final String weatherSite = "https://api.openweathermap.org";
    private final String apiKey = "80b8b51878e4ae64fc72d800c1679d04";
    private final String UNITS = "units";
    private final String CELCSIUS = "celsius";
    private OpenWeatherAPI openWeatherAPI;
    private OpenWeatherTaskApi OpenWeatherTaskApi;
    private String units;
    private RetrofitCurrentPositionRequest retrofitCurrentPositionRequest;
    private String longitude;
    private String latitude;
    private String weatherSaved;
    private String citySaved;
    private String tmpSaved;
    private boolean checkPoint = false;
    private ProgressBar locationProgressBar;
    private MapView mapView;
    private GoogleMap gMap;

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        System.out.println("Changed property: " + propertyChangeEvent.getPropertyName() + " [old -> "
                + propertyChangeEvent.getOldValue() + "] | [new -> " + propertyChangeEvent.getNewValue() + "]");
        flat = propertyChangeEvent.getNewValue().toString();
    }

    public InitialFragment(Manager manager) {
        manager.addChangeListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        gMap .getUiSettings().setZoomControlsEnabled(true);
        gMap .addMarker(new MarkerOptions().position(sydney));
        gMap .moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));
    }

    public interface OnItemClickedListener {
        public void OnItemClicked(Policy.Parameters params);
    }

    public InitialFragment() {
        // Required empty public constructor
    }

    public static InitialFragment newInstance(String param1, String param2) {

        InitialFragment fragment = new InitialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        units = SharedPreferencesClass.getData(getContext(), UNITS).contains(CELCSIUS) ? METRIC : IMPERIAL;
        retrofitCurrentPositionRequest = new RetrofitCurrentPositionRequest(getContext());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
        if (SettingsStorage.getFlagMode().equals("night")) {
            activity.findViewById(R.id.cityMainLinearLayout).setBackgroundColor(Color.rgb(173, 181, 189));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.initial_fragment, container, false);
        cardView = view.findViewById(R.id.cardViewCurrentCity);
        dayTmp = view.findViewById(R.id.dayTempInt);
        dayTmp.setVisibility(View.GONE);
        waetherDescr = view.findViewById(R.id.nightTempInt);
        waetherDescr.setVisibility(View.GONE);
        cityInitTxt = view.findViewById(R.id.cityInitTxt);
        cityInitTxt.setVisibility(View.GONE);
        favTitle = view.findViewById(R.id.searchTitle);
        favTitle.setVisibility(View.GONE);
        locationProgressBar  = view.findViewById(R.id.progressBarLocation);
        locationProgressBar.setMax(50);
        locationProgressBar.setProgress(20);
        locationProgressBar.getProgressDrawable().setColorFilter(
                Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
        requestPemissions();
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 111) {
            if (requestCode == 111) {
                Toast.makeText(getActivity(), data.getStringExtra("test"), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        String mode = SharedPreferencesClass.getData(getContext(), "mySetting");
        if (!mode.contains("opppss, there is no data found")) {

            if (view != null) {
                view.findViewById(R.id.initialFragmentId).setBackgroundColor(Color.rgb(173, 181, 189));
                view.findViewById(R.id.initialLinearLayoutID).setBackgroundColor(Color.rgb(173, 181, 189));
            }

        }
        getView().setBackgroundColor(Color.WHITE);
        if (!checkPoint) {
            dayTmp.setText(tmpSaved);
            waetherDescr.setText(weatherSaved);
            cityInitTxt.setText(citySaved);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        weatherSaved = (String) waetherDescr.getText();
        citySaved = (String) cityInitTxt.getText();
        tmpSaved = (String) dayTmp.getText();
        checkPoint = true;
    }

    //если не стоит показывать по местоположение, то здесь будет реализация по последнему городу. Пока не доделано
    private void requestRetrofit(final String city, final String units, final String keyApi) {

        openWeatherAPI.loadData(city, units, keyApi).enqueue(new Callback<WeatherParsing>() {
            @Override
            public void onResponse(Call<WeatherParsing> call, Response<WeatherParsing> response) {
                if (response.body() != null) {
                    NumberFormat nf = NumberFormat.getCurrencyInstance();
                    nf.setMaximumFractionDigits(0);
                    String cutOffTemp = nf.format(response.body().getMain().getTemp()).replaceAll("[$]", "");
                    dayTmp.setVisibility(View.VISIBLE);
                    waetherDescr.setVisibility(View.VISIBLE);
                    cityInitTxt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<WeatherParsing> call, Throwable t) {
                //TODO ERROR
            }
        });

    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 10000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    double lat = location.getLatitude();
                    latitude = Double.toString(lat);
                    double lng = location.getLongitude();
                    longitude = Double.toString(lng);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            retrofitCurrentPositionRequest.requestRetrofit(latitude, longitude);

                            try {
                                Thread.sleep(1000);
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String tmp = retrofitCurrentPositionRequest.getCityTmp();
                                        dayTmp.setText(tmp);
                                        dayTmp.setVisibility(View.VISIBLE);

                                        String city = retrofitCurrentPositionRequest.getCityName();
                                        cityInitTxt.setText(city);
                                        cityInitTxt.setVisibility(View.VISIBLE);

                                        String description = retrofitCurrentPositionRequest.getWeatherDscrp();
                                        waetherDescr.setText(description);
                                        waetherDescr.setVisibility(View.VISIBLE);
                                        locationProgressBar.setVisibility(View.GONE);
                                    }
                                });
                            } catch (Exception ignored) {
                            }
                        }
                    }).start();

                }

            });
        }
    }

    private void requestPemissions() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();

        } else {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length == 2 && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                requestLocation();
            }
        }
    }

}
