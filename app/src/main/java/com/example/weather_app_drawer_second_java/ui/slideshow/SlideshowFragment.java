package com.example.weather_app_drawer_second_java.ui.slideshow;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.weather_app_drawer_second_java.R;
import com.example.weather_app_drawer_second_java.weatherApp.App;
import com.example.weather_app_drawer_second_java.weatherApp.SharedPreferencesClass;

import com.google.android.material.snackbar.Snackbar;
// TODO (1°C × 9/5) + 32 = 33.8°F

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    SharedPreferences sharedPreferences;
    public static final String SETTING = "mySetting";
    public static final String  UNITS = "units";
    public static final String NIGHT_MODE = "night";
    public static final String CELSIUS = "celsius";
    public static final String FAHRENHEIT = "fahrenheit";

   private Switch switcher;
    private  RadioGroup radioGroup;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
   /*     final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });          */
        radioGroup = (RadioGroup)root.findViewById(R.id.tempretureRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.celsiusRadioButton:
                        Toast.makeText(getContext(), "Celsius",
                                Toast.LENGTH_SHORT).show();
                        SharedPreferencesClass.insertData(getContext(),UNITS,CELSIUS);
                        break;
                    case R.id.fahrenheitRadioButton:
                        Toast.makeText(getContext(), "Fahrenheit",
                                Toast.LENGTH_SHORT).show();
                        SharedPreferencesClass.insertData(getContext(),UNITS,FAHRENHEIT);
                    default:
                        break;
                }
            }
        });



        switcher = (Switch) root.findViewById(R.id.nightModeSwitch);


        if (switcher != null) {
            switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {

                        SharedPreferencesClass.insertData(getContext(),SETTING,NIGHT_MODE);
                        root.findViewById(R.id.setttingFragment).setBackgroundColor(Color.rgb(173, 181, 189));

                    } else {
                        root.findViewById(R.id.setttingFragment).setBackgroundColor(Color.WHITE);
                        // view.findViewById(R.id.cityWeatherFrameID).setBackgroundColor(Color.WHITE);
                    }

                }
            });

        }






        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());

    }

    @Override
    public void onResume() {
        super.onResume();
        String mode = SharedPreferencesClass.getData(getContext(),"mySetting");
        if(!mode.contains("opppss, there is no data found")){
            getView().setBackgroundColor(Color.rgb(173, 181, 189));
            switcher.setOnCheckedChangeListener(null);
            switcher.setChecked(true);
            switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {

                        SharedPreferencesClass.insertData(getContext(),SETTING,NIGHT_MODE);
                        getView().setBackgroundColor(Color.rgb(173, 181, 189));

                    } else {
                     getView().setBackgroundColor(Color.WHITE);
                     SharedPreferencesClass.deleteData(getContext(),"mySetting");
                        // view.findViewById(R.id.cityWeatherFrameID).setBackgroundColor(Color.WHITE);
                    }

                }
            });
        }
        String units = SharedPreferencesClass.getData(getContext(),"units");
        if(units.contains(CELSIUS)){
          radioGroup.setOnCheckedChangeListener(null);
          radioGroup.check(R.id.celsiusRadioButton);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch(i){
                        case R.id.celsiusRadioButton:
                            Toast.makeText(getContext(), "Celsius",
                                    Toast.LENGTH_SHORT).show();
                            SharedPreferencesClass.insertData(getContext(),UNITS,CELSIUS);
                            break;
                        case R.id.fahrenheitRadioButton:
                            Toast.makeText(getContext(), "Fahrenheit",
                                    Toast.LENGTH_SHORT).show();
                            SharedPreferencesClass.insertData(getContext(),UNITS,FAHRENHEIT);
                        default:
                            break;
                    }
                }
            });

        } else {
            radioGroup.setOnCheckedChangeListener(null);
            radioGroup.check(R.id.fahrenheitRadioButton);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch(i){
                        case R.id.celsiusRadioButton:
                            Toast.makeText(getContext(), "Celsius",
                                    Toast.LENGTH_SHORT).show();
                            SharedPreferencesClass.insertData(getContext(),UNITS,CELSIUS);
                            break;
                        case R.id.fahrenheitRadioButton:
                            Toast.makeText(getContext(), "Fahrenheit",
                                    Toast.LENGTH_SHORT).show();
                            SharedPreferencesClass.insertData(getContext(),UNITS,FAHRENHEIT);
                        default:
                            break;
                    }
                }
            });
        }

    }
}