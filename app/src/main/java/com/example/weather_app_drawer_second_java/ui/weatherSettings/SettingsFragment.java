package com.example.weather_app_drawer_second_java.ui.weatherSettings;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.weather_app_drawer_second_java.NotificationWeatherIntentService;
import com.example.weather_app_drawer_second_java.R;
import com.example.weather_app_drawer_second_java.weatherApp.SharedPreferencesClass;


public class SettingsFragment extends Fragment {
    SharedPreferences sharedPreferences;
    private SettingsViewModel settingsViewModel;
    public static final String SETTING = "mySetting";
    public static final String UNITS = "units";
    public static final String NIGHT_MODE = "night";
    public static final String CELSIUS = "celsius";
    public static final String FAHRENHEIT = "fahrenheit";
    public static final String RAIN_ALERT = "rain";
    public static final String ALERTS = "rain_alert";
    private Switch switcher;
    private RadioGroup radioGroup;
    private Switch rainNotificationSwitch;
    static final String BROADCAST_ACT = "com.example.weather_app_drawer_second_java.service.done";
    private final static String BROADCAST_TAG = "weatherBroadcast";


    private BroadcastReceiver weatherUpload = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           Log.d(BROADCAST_TAG,"works");
        }
    };


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_slideshow, container, false);


        rainNotificationSwitch = (Switch) root.findViewById(R.id.rainNotifiication);
        if (rainNotificationSwitch != null) {
            rainNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        getActivity().startService(new Intent(getActivity(), NotificationWeatherIntentService.class));
                        NotificationWeatherIntentService.startActionFoo(getContext(), ALERTS);
                        SharedPreferencesClass.insertData(getContext(), ALERTS, RAIN_ALERT);
                    } else {
                        Intent serviceIntent = new Intent(getActivity(), NotificationWeatherIntentService.class);
                        getActivity().stopService(serviceIntent);
                    }
                }
            });
        }

        radioGroup = (RadioGroup) root.findViewById(R.id.tempretureRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.celsiusRadioButton:
                        Toast.makeText(getContext(), "Celsius",
                                Toast.LENGTH_SHORT).show();
                        SharedPreferencesClass.insertData(getContext(), UNITS, CELSIUS);
                        break;
                    case R.id.fahrenheitRadioButton:
                        Toast.makeText(getContext(), "Fahrenheit",
                                Toast.LENGTH_SHORT).show();
                        SharedPreferencesClass.insertData(getContext(), UNITS, FAHRENHEIT);
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
                        SharedPreferencesClass.insertData(getContext(), SETTING, NIGHT_MODE);
                        root.findViewById(R.id.setttingFragment).setBackgroundColor(Color.rgb(173, 181, 189));

                    } else {
                        root.findViewById(R.id.setttingFragment).setBackgroundColor(Color.WHITE);
                    }

                }
            });

        }

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(weatherUpload, new IntentFilter(BROADCAST_ACT));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(weatherUpload);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNotificationChannel();
    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel myChannel = new NotificationChannel("2", "weather", importance);
            notificationManager.createNotificationChannel(myChannel);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String mode = SharedPreferencesClass.getData(getContext(), "mySetting");
        String units = SharedPreferencesClass.getData(getContext(), "units");
        String alert = SharedPreferencesClass.getData(getContext(), ALERTS);

        if (!mode.contains("opppss, there is no data found")) {
            getView().setBackgroundColor(Color.rgb(173, 181, 189));
            switcher.setOnCheckedChangeListener(null);
            switcher.setChecked(true);
            switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        SharedPreferencesClass.insertData(getContext(), SETTING, NIGHT_MODE);
                        getView().setBackgroundColor(Color.rgb(173, 181, 189));

                    } else {
                        getView().setBackgroundColor(Color.WHITE);
                        SharedPreferencesClass.deleteData(getContext(), "mySetting");
                    }

                }
            });
        }
        if (!alert.contains("opppss, there is no data found")) {
            Intent serviceIntent = new Intent(getActivity(), NotificationWeatherIntentService.class);
            getActivity().stopService(serviceIntent);
            rainNotificationSwitch.setOnCheckedChangeListener(null);
            rainNotificationSwitch.setChecked(false);
            rainNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        //сделать проверку запущена ли служда,если нет, то запустить
                        getActivity().startService(new Intent(getActivity(), NotificationWeatherIntentService.class));
                        NotificationWeatherIntentService.startActionFoo(getContext(), ALERTS);
                        SharedPreferencesClass.insertData(getContext(), ALERTS, RAIN_ALERT);

                    } else {
                        SharedPreferencesClass.deleteData(getContext(), ALERTS);
                        Intent serviceIntent = new Intent(getActivity(), NotificationWeatherIntentService.class);
                        getActivity().stopService(serviceIntent);
                    }
                }
            });
        } else {

            rainNotificationSwitch.setOnCheckedChangeListener(null);
            rainNotificationSwitch.setChecked(true);
            getActivity().startService(new Intent(getActivity(), NotificationWeatherIntentService.class));
            NotificationWeatherIntentService.startActionFoo(getContext(), ALERTS);
            rainNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        //сделать проверку запущена ли служда,если нет, то запустить
                        getActivity().startService(new Intent(getActivity(), NotificationWeatherIntentService.class));
                        NotificationWeatherIntentService.startActionFoo(getContext(), ALERTS);
                        SharedPreferencesClass.insertData(getContext(), ALERTS, RAIN_ALERT);

                    } else {
                        SharedPreferencesClass.deleteData(getContext(), ALERTS);
                        Intent serviceIntent = new Intent(getActivity(), NotificationWeatherIntentService.class);
                        getActivity().stopService(serviceIntent);
                    }
                }
            });

        }
        if (units.contains(CELSIUS)) {
            radioGroup.setOnCheckedChangeListener(null);
            radioGroup.check(R.id.celsiusRadioButton);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i) {
                        case R.id.celsiusRadioButton:
                            Toast.makeText(getContext(), "Celsius",
                                    Toast.LENGTH_SHORT).show();
                            SharedPreferencesClass.insertData(getContext(), UNITS, CELSIUS);
                            break;
                        case R.id.fahrenheitRadioButton:
                            Toast.makeText(getContext(), "Fahrenheit",
                                    Toast.LENGTH_SHORT).show();
                            SharedPreferencesClass.insertData(getContext(), UNITS, FAHRENHEIT);
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
                    switch (i) {
                        case R.id.celsiusRadioButton:
                            Toast.makeText(getContext(), "Celsius",
                                    Toast.LENGTH_SHORT).show();
                            SharedPreferencesClass.insertData(getContext(), UNITS, CELSIUS);
                            break;
                        case R.id.fahrenheitRadioButton:
                            Toast.makeText(getContext(), "Fahrenheit",
                                    Toast.LENGTH_SHORT).show();
                            SharedPreferencesClass.insertData(getContext(), UNITS, FAHRENHEIT);
                        default:
                            break;
                    }
                }
            });
        }

    }
}