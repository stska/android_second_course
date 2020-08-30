package com.example.weather_app_drawer_second_java.weatherApp;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SettingsStorage implements PropertyChangeListener {
    private static String flagMode = "day";

    public SettingsStorage(Manager manager) {
        manager.addChangeListener(this);
    }

    public static void setFlagMode(String flagMode) {
        SettingsStorage.flagMode = flagMode;
    }

    public static String getFlagMode() {
        return flagMode;
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        System.out.println("Измененное поле: " + propertyChangeEvent.getPropertyName() + " [старое значение -> "
                + propertyChangeEvent.getOldValue() + "] | [новое значение -> " + propertyChangeEvent.getNewValue() + "]");

    }
}
