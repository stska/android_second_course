package com.example.weather_app_drawer_second_java.ui.weatherSettings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is weatherSettings fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}