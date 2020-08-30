package com.example.weather_app_drawer_second_java.weatherApp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_app_drawer_second_java.R;
import com.example.weather_app_drawer_second_java.ui.slideshow.SlideshowFragment;
import com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass.Example2;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.Policy;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InitialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InitialFragment extends Fragment implements PropertyChangeListener {
    OnItemClickedListener mListener;
    //private    Manager manager;
    private static String flat = "";
    Manager manager = new Manager();
    private Example2[] example2s;

    SharedPreferences sharedPreferences;

    SingltoneListOfCities singltoneListOfCities;

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

    WeatherInfo weatherInfo = new WeatherInfo();


    private FragmentActivity myContext;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;




    public InitialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment initialFragment.
     */
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
        //example2s =   listOfCities();
        Resources res = getResources();
        singltoneListOfCities = SingltoneListOfCities.getInstance(res);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    //changed 08.07.2020 //------------------------------------------------хммммммммммммммммммммммммммммммммммм
    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
        if(SettingsStorage.getFlagMode().equals("night")){
            activity.findViewById(R.id.cityMainLinearLayout).setBackgroundColor(Color.rgb(173, 181, 189));
        }


    }

    private boolean compareQueryToList(String query, Example2[] example2){

        for(int i = 0; i < example2.length;i++){
            if(example2[i].getName().equals(query)){

                return true;
            }

        }
        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.initial_fragment, container, false);





        final SearchView searchView = (SearchView) view.findViewById(R.id.searchViewId);



        //обработка ввода текста и его отправка
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {



                boolean checkUp = false;

                if (compareQueryToList(query,singltoneListOfCities.example2)) {
                   // goTo(position);
                    goTo(query);
                    searchView.clearFocus(); //-------------------------------------------
                    return true;
                } else Toast.makeText(getActivity(), "No Match found", Toast.LENGTH_LONG).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });



        return view;

    }

    private void goTo(String city) {
        CityWeatherDescription cwd = CityWeatherDescription.newInstance(city);
        // cwd.setTargetFragment(InitialFragment.this,111);
        ///--------------------------------------------------
        FragmentTransaction ft = ((AppCompatActivity) myContext).getSupportFragmentManager().beginTransaction()
                .replace(R.id.initialFramLayoutId, cwd)
                .addToBackStack(cwd.getClass().getName())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);    //--------------changed 08.07.2020

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
            String mode = SharedPreferencesClass.getData(getContext(),"mySetting");
            if(!mode.contains("opppss, there is no data found")){
                System.out.println("WORKSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
               // getView().setBackgroundColor(Color.rgb(173, 181, 189));

                if (view != null) {
                   view.findViewById(R.id.initialFragmentId).setBackgroundColor(Color.rgb(173, 181, 189));
                    view.findViewById(R.id.initialLinearLayoutID).setBackgroundColor(Color.rgb(173, 181, 189));


                }

            } getView().setBackgroundColor(Color.WHITE);
        if(!WeatherHistory.weatherHistories.isEmpty()) {
            initRecycleView(view);
        }

    }
    private void initRecycleView(View view) {

        RecyclerView recyclerView = view.findViewById(R.id.favCitiesRecycleView);
        recyclerView.setHasFixedSize(true);



        LinearLayoutManager layoutManager = new LinearLayoutManager(myContext);
        recyclerView.setLayoutManager(layoutManager);



        FavRecycleViewAdapt adapter = new FavRecycleViewAdapt(getContext());
        recyclerView.setAdapter(adapter);


    }

}

