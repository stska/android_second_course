package com.example.weather_app_drawer_second_java.weatherApp;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_app_drawer_second_java.OpenWeatherAPI;
import com.example.weather_app_drawer_second_java.R;
import com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass.WeatherParsing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CityWeatherDescription#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CityWeatherDescription extends Fragment implements PropertyChangeListener {
    private FragmentActivity myContext;
    private static final String ARG_PARAM2 = "param2";
    private String cityName = "";
    private String cityTmp = "";
    private String cityHum = "";
    private String cityWind = "";
    private String cityPres = "";
    private TextView cityNameText;
    private TextView cityTempText;
    private TextView cityHumidText;
    private TextView cityPressureText;
    private TextView cityWindSpeedText;
    private WeatherHistory history;
    private ProgressBar progressBar;
    private String weatherDscrp;
    private String units;
    private int mParam1;
    private String mParam2;
    private String icon;
    InitialFragment.OnItemClickedListener mListener;
    private final String METRIC = "metric";
    private final String IMPERIAL = "";
    private final String weatherSite = "https://api.openweathermap.org";
    private final String apiKey = "80b8b51878e4ae64fc72d800c1679d04";
    private final String UNITS = "units";
    private final String CELCSIUS = "celsius";
    private OpenWeatherAPI openWeatherAPI;

    public CityWeatherDescription() {
        // Required empty public constructor
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        System.out.println("Changed property: " + propertyChangeEvent.getPropertyName() + "old :" + propertyChangeEvent.getOldValue() + "and new :" + propertyChangeEvent.getNewValue());
    }


    public static CityWeatherDescription newInstance(String mParam2) {
        CityWeatherDescription fragment = new CityWeatherDescription();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM2, mParam2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        units = SharedPreferencesClass.getData(getContext(),UNITS).contains(CELCSIUS) ? METRIC : IMPERIAL;
        initRetrofit();
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
            requestRetrofit(mParam2,units,apiKey);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_city_weather_description, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBarWeatherDesc);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        cityNameText = (TextView) view.findViewById(R.id.cityNameView);
        cityTempText = (TextView) view.findViewById(R.id.cityTempTextView);
        cityHumidText = (TextView) view.findViewById(R.id.humidityTextView);
        cityPressureText = (TextView) view.findViewById(R.id.pressureTextView);
        cityWindSpeedText = (TextView) view.findViewById(R.id.windSpeedTextView);
        initRecycleView(view);

        return view;
    }

    private void initRecycleView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.weatherRecyleViewFiveDays);
        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.GONE); //так как недоделана передача данных и чтобы не оставлять их пустыми на экране, пока закоментированно

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayout.HORIZONTAL & LinearLayout.VERTICAL);
        itemDecoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.separator, null));
        recyclerView.addItemDecoration(itemDecoration);

        LinearLayoutManager layoutManager = new LinearLayoutManager(myContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        TempRecycleVoewAdapter adapter = new TempRecycleVoewAdapter();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void initRetrofit(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(weatherSite).addConverterFactory(GsonConverterFactory.create()).build();
        openWeatherAPI = retrofit.create(OpenWeatherAPI.class);
    }
    private void requestRetrofit(final String city,final String units,final String keyApi){

        openWeatherAPI.loadData(city,units,keyApi).enqueue(new Callback<WeatherParsing>() {
            @Override
            public void onResponse(Call<WeatherParsing> call, Response<WeatherParsing> response) {
                if(response.body() != null){
                    //TODO
                    String test = response.body().getMain().getTemp().toString();
                    System.out.println(test);

                    weatherDscrp = response.body().getWeather().get(0).getDescription();
                    cityName = response.body().getName();
                    cityTmp = response.body().getMain().getTemp().toString();
                    cityHum = response.body().getMain().getHumidity().toString().concat("%");
                    cityWind =response.body().getWind().getSpeed().toString();
                    cityPres = response.body().getMain().getPressure().toString().concat("hPa");
                    icon = response.body().getWeather().get(0).getIcon();

                    history = new WeatherHistory(cityName, cityTmp, cityPres, weatherDscrp, cityHum,icon);

                    if (!WeatherHistory.weatherHistories.isEmpty()) {
                        for (int i = 0; i < WeatherHistory.weatherHistories.size(); i++) {
                            System.out.println(WeatherHistory.weatherHistories.get(i).getCityName());
                        }
                    }
                    cityNameText.setText(cityName);
                    cityTempText.setText(cityTmp.concat("\u00B0"));
                    cityHumidText.setText(cityHum);
                    cityPressureText.setText(cityPres);
                    cityWindSpeedText.setText(cityWind.toString());
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<WeatherParsing> call, Throwable t) {
                //TODO ERROR
            }
        });


    }


}
