package com.example.weather_app_drawer_second_java.weatherApp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
import com.example.weather_app_drawer_second_java.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.Policy;


public class InitialFragment extends Fragment implements PropertyChangeListener, OnMapReadyCallback {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String flat = "";
    private final String METRIC = "metric";
    private final String IMPERIAL = "";
    private final String weatherSite = "https://api.openweathermap.org";
    private final String apiKey = "80b8b51878e4ae64fc72d800c1679d04";
    private final String UNITS = "units";
    private final String CELCSIUS = "celsius";
    private CardView cardView;
    private TextView dayTmp;
    private TextView waetherDescr;
    private TextView cityInitTxt;
    private FragmentActivity myContext;
    private SingltoneListOfCities singltoneListOfCities;
    private TextView favTitle;
    private String mParam1;
    private String mParam2;
    private com.example.weather_app_drawer_second_java.OpenWeatherTaskApi OpenWeatherTaskApi;
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
    private double lat;
    private double lng;

    public InitialFragment(Manager manager) {
        manager.addChangeListener(this);
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
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        System.out.println("Changed property: " + propertyChangeEvent.getPropertyName() + " [old -> "
                + propertyChangeEvent.getOldValue() + "] | [new -> " + propertyChangeEvent.getNewValue() + "]");
        flat = propertyChangeEvent.getNewValue().toString();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.addMarker(new MarkerOptions().position(sydney));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPemissions();
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
        locationProgressBar = view.findViewById(R.id.progressBarLocation);
        locationProgressBar.setMax(50);
        locationProgressBar.setProgress(20);
        locationProgressBar.getProgressDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                MapState mapState = new MapState(getContext());
                CameraPosition position = mapState.getSavedCameraPosition();
                if (position != null) {
                    CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
                    Toast.makeText(getContext(), "entering Resume State", Toast.LENGTH_SHORT).show();
                    gMap.moveCamera(update);
                    gMap.setMapType(mapState.getSavedMapType());
                } else {
                    LatLng location = new LatLng(lat, lng);
                    googleMap.addMarker(new MarkerOptions().position(location).title("Marker Title").snippet("Marker Description"));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        });
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
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        weatherSaved = (String) waetherDescr.getText();
        citySaved = (String) cityInitTxt.getText();
        tmpSaved = (String) dayTmp.getText();
        checkPoint = true;
        MapState mapState = new MapState(getContext());
        mapState.saveMapState(gMap);
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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
                    lat = location.getLatitude();
                    latitude = Double.toString(lat);
                    lng = location.getLongitude();
                    longitude = Double.toString(lng);
                    setCardView();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

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

    public interface OnItemClickedListener {
        public void OnItemClicked(Policy.Parameters params);
    }

    private void setCardView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                retrofitCurrentPositionRequest.requestRetrofit(latitude, longitude);
                try {
                    Thread.sleep(2000);
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

}
