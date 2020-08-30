package com.example.weather_app_drawer_second_java.weatherApp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Set;

public class Manager  {
   public static final String MODE = "Day Mode";
  //public static  String MODE = "Day Mode";

   private List<Settings> setting = new ArrayList<>();
   private List<PropertyChangeListener> listener = new ArrayList<>();

   public class Settings {
       private String mode;

       public Settings(String mode) {
           this.mode = mode;
       }

       public String getMode() {
           return mode;
       }

       public void setMode(String mode) {
           notifyListeners(this, MODE, this.mode, this.mode = mode);
       }
   }
       public List<Settings> getSettings(){
           return setting;
       }
       public Manager(){
         setting.add(new Settings("Strange mode"));
       }
       private void notifyListeners(Object object,String mode,String oldMode,String newMode){
           for(PropertyChangeListener setting : listener){
               setting.propertyChange(new PropertyChangeEvent(this, mode, oldMode,newMode ));
           }
       }
       public void addChangeListener(PropertyChangeListener newListener){
           listener.add(newListener);
       }

}
