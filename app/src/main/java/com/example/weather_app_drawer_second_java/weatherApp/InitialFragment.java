package com.example.weather_app_drawer_second_java.weatherApp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_app_drawer_second_java.R;
import com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass.Example2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.security.Policy;


public class InitialFragment extends Fragment implements PropertyChangeListener {
    OnItemClickedListener mListener;

    private static String flat = "";
    private Manager manager = new Manager();
    private Example2[] example2s;

    SharedPreferences sharedPreferences;
    private CardView cardView;
    private TextView dayTmp;
    private TextView nightTmp;
    private TextView cityInitTxt;
    private FragmentActivity myContext;
    private SingltoneListOfCities singltoneListOfCities;

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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        try {
            singltoneListOfCities = SingltoneListOfCities.getInstance(res);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        cardView.setVisibility(View.GONE);
        dayTmp = view.findViewById(R.id.dayTempInt);
        nightTmp = view.findViewById(R.id.nightId);
        cityInitTxt = view.findViewById(R.id.cityInitTxt);

        return view;
    }

    private void goTo(String city) {
        CityWeatherDescription cwd = CityWeatherDescription.newInstance(city);

        FragmentTransaction ft = ((AppCompatActivity) myContext).getSupportFragmentManager().beginTransaction()
                .replace(R.id.initialFramLayoutId, cwd)
                .addToBackStack(cwd.getClass().getName())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        ft.commit();
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
        if (!WeatherHistory.weatherHistories.isEmpty()) {
            initRecycleView(view);
        }
        if (WeatherHistory.weatherHistories.size() > 0) {
            int lastTmpSize = WeatherHistory.weatherHistories.size() - 1;
            cardView.setVisibility(View.VISIBLE);
            dayTmp.setText(WeatherHistory.weatherHistories.get(lastTmpSize).getCityTmp().concat("\u00B0"));
            cityInitTxt.setText(WeatherHistory.weatherHistories.get(lastTmpSize).getCityName());
        }

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

}

