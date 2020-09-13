package com.example.weather_app_drawer_second_java.weatherApp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_app_drawer_second_java.OpenWeatherAPI;
import com.example.weather_app_drawer_second_java.R;
import com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass.WeatherParsing;
import com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass.WeatherParsingVersionTwo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.security.Policy;
import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class InitialFragment extends Fragment implements PropertyChangeListener {
    OnItemClickedListener mListener;
    private static String flat = "";
    private WeatherParsingVersionTwo[] example2s;
    private CardView cardView;
    private TextView dayTmp;
    private TextView nightTmp;
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
    private String units;

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        System.out.println("Changed property: " + propertyChangeEvent.getPropertyName() + " [old -> "
                + propertyChangeEvent.getOldValue() + "] | [new -> " + propertyChangeEvent.getNewValue() + "]");
        flat = propertyChangeEvent.getNewValue().toString();
    }

    public InitialFragment(Manager manager) {
        manager.addChangeListener(this);
    }

    public interface OnItemClickedListener {
        public void OnItemClicked(Policy.Parameters params);
    }

    public InitialFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static InitialFragment newInstance(String param1, String param2) {

        InitialFragment fragment = new InitialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(weatherSite).addConverterFactory(GsonConverterFactory.create()).build();
        openWeatherAPI = retrofit.create(OpenWeatherAPI.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        units = SharedPreferencesClass.getData(getContext(), UNITS).contains(CELCSIUS) ? METRIC : IMPERIAL;
        try {
            singltoneListOfCities = SingltoneListOfCities.getInstance(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initRetrofit();
        requestRetrofit(SharedPreferencesClass.getData(getContext(), "city"), units, apiKey);
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
        nightTmp = view.findViewById(R.id.nightTempInt);
        nightTmp.setVisibility(View.GONE);
        cityInitTxt = view.findViewById(R.id.cityInitTxt);
        cityInitTxt.setVisibility(View.GONE);
        favTitle = view.findViewById(R.id.searchTitle);
        favTitle.setVisibility(View.GONE);
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
    }

    private void initRecycleView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.favCitiesRecycleView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(myContext);
        recyclerView.setLayoutManager(layoutManager);

        FavRecycleViewAdapt adapter = new FavRecycleViewAdapt(getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void requestRetrofit(final String city, final String units, final String keyApi) {

        openWeatherAPI.loadData(city, units, keyApi).enqueue(new Callback<WeatherParsing>() {
            @Override
            public void onResponse(Call<WeatherParsing> call, Response<WeatherParsing> response) {
                if (response.body() != null) {
                    NumberFormat nf = NumberFormat.getCurrencyInstance();
                    nf.setMaximumFractionDigits(0);
                    String cutOffTemp = nf.format(response.body().getMain().getTemp()).replaceAll("[$]", "");
                    dayTmp.setText(cutOffTemp.concat("\u00B0"));
                    dayTmp.setVisibility(View.VISIBLE);
                    nightTmp.setText(response.body().getWeather().get(0).getDescription());
                    nightTmp.setVisibility(View.VISIBLE);
                    cityInitTxt.setText(response.body().getName());
                    cityInitTxt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<WeatherParsing> call, Throwable t) {
                //TODO ERROR
            }
        });

    }

}

