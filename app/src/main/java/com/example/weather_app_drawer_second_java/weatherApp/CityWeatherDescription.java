package com.example.weather_app_drawer_second_java.weatherApp;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_app_drawer_second_java.R;
import com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass.Example;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CityWeatherDescription#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CityWeatherDescription extends Fragment implements PropertyChangeListener, LoaderManager.LoaderCallbacks<Example> {
    private FragmentActivity myContext;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String cityName = "";
    private String cityTmp = "";
    private String cityHum = "";
    private String cityWind = "";
    private String cityPres = "";
    TextView cityNameText ;
    TextView cityTempText ;
    TextView cityHumidText ;
    TextView cityPressureText ;
    TextView cityWindSpeedText ;

    private  ProgressBar progressBar;

    WeatherInfo weatherInfo = new WeatherInfo();
    private final float FROMFTOC = (float) 273.15;


    // TODO: Rename and change types of parameters
    private int mParam1;
     private String mParam2;

    public CityWeatherDescription() {
        // Required empty public constructor
    }
    String flat = "";

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        System.out.println("Changed property: " + propertyChangeEvent.getPropertyName() + "old :" + propertyChangeEvent.getOldValue() + "and new :" + propertyChangeEvent.getNewValue());
    }
    //--------------------------------------------------------------------------------------------------------21.07
    @NonNull
    @Override
    public Loader<Example> onCreateLoader(int id, @Nullable Bundle argsm) {
            return new LoaderClass(getContext(), mParam2,progressBar);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Example> loader, Example data) {
      //  loader.startLoading();
        cityName = data.getName();
        cityTmp = data.getMain().getTemp().toString();
        cityHum = data.getMain().getHumidity().toString();
        cityWind = data.getWind().getSpeed().toString();
        cityPres = data.getMain().getPressure().toString();

        WeatherHistory history = new WeatherHistory(cityName,cityTmp,cityPres,data.getWeather().get(0).getDescription().toString());
        if(!WeatherHistory.weatherHistories.isEmpty()){
            for(int i = 0; i < WeatherHistory.weatherHistories.size();i ++){
                System.out.println(WeatherHistory.weatherHistories.get(i).getCityName());
            }
        }

        cityNameText.setText(cityName);
        cityTempText.setText(cityTmp);
        cityHumidText.setText(cityHum);
        cityPressureText.setText(cityPres);
        cityWindSpeedText.setText(cityWind.toString());
        progressBar.setVisibility(ProgressBar.INVISIBLE);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Example> loader) {
        if(!WeatherHistory.weatherHistories.isEmpty()){
            for(int i = 0; i < WeatherHistory.weatherHistories.size();i ++){
                System.out.println(WeatherHistory.weatherHistories.get(i).getCityName());
            }
        }
    }
    //-------------------------------------------------------------------------------------------

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * //@param param1 Parameter 1.
     *  //@param param2 Parameter 2.
     * @return A new instance of fragment CityWeatherDescription.
     */
    // TODO: Rename and change types and number of parameters
    // public static CityWeatherDescription newInstance(String param1, String param2) {
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
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getLoaderManager().initLoader(1, null,this).forceLoad();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        final View view = inflater.inflate(R.layout.fragment_city_weather_description, container, false);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBarWeatherDesc);
        progressBar.setVisibility(ProgressBar.VISIBLE);
       cityNameText = (TextView) view.findViewById(R.id.cityNameView);
        cityTempText = (TextView) view.findViewById(R.id.cityTempTextView);
        cityHumidText = (TextView) view.findViewById(R.id.humidityTextView);
    cityPressureText = (TextView) view.findViewById(R.id.pressureTextView);
        cityWindSpeedText = (TextView) view.findViewById(R.id.windSpeedTextView);

        initRecycleView(view);
        return view;


    }

    InitialFragment.OnItemClickedListener mListener;


    private void initRecycleView(View view) {

        RecyclerView recyclerView = view.findViewById(R.id.weatherRecyleViewFiveDays);
        recyclerView.setHasFixedSize(true);

        // getTargetFragment().onActivityResult(getTargetRequestCode(),Activity.RESULT_OK,new Intent().putExtra("datafrom C", "datafrom C"));


        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayout.HORIZONTAL & LinearLayout.VERTICAL);
        itemDecoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.separator, null));
        recyclerView.addItemDecoration(itemDecoration);

        //DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayout.VERTICAL);
        // itemDecoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_ripple,null));
        // recyclerView.addItemDecoration(itemDecoration);

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


}
