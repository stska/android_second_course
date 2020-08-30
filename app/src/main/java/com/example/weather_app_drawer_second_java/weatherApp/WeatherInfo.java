package com.example.weather_app_drawer_second_java.weatherApp;


//Пока этот класс - модель для активити с детальным описанием погода и этот класс сейчас не прикруче и используется только чутка.
//TODO прикрутить полноценно класс

import com.example.weather_app_drawer_second_java.R;

public class WeatherInfo {
    private String cityName;
    private String cityTemp;
    private String cityHumidity;
    private String cityPressure;
    private  String cityWindSpeed;

    private String [] cityTempFiveDays;
    private  String[] cityHumFiveDays;


    private String[] cityNamesArray = {
            "Moscow",
            "St.Petersburg",
            "Warsaw",
            "New York",
            "Sydney",
            "Tokyo"
    };
    private Integer[] imagesArray = {
            R.drawable.rainbow,
            R.drawable.sun,
            R.drawable.wind,
            R.drawable.winter,
            R.drawable.sunglasses,
            R.drawable.moon
    };
    private String[] weatherArray = {
            "Rainy",
            "Sunny",
            "Windy",
            "Frosty",
            "Heat",
            "Mild"
    };
    private String[] temperatureArray = {
            "15",
            "24",
            "10",
            "-10",
            "+40",
            "+5"
    };
    private String[] humidityArray = {
            "59 %",
            "65 %",
            "45 %",
            "50 %",
            "80 %",
            "76 %"
    };
    private String[] pressureArray = {
            "743 мм. р",
            "600 мм. р",
            "845 мм. р",
            "665 mm р",
            "720 mm p",
            "7600 mm p"
    };
    private String[] windArray = {
            "10 m/s",
            "1 m/s",
            "5 m/s",
            "3 m/s",
            "4 m/s",
            "6 m/s"
    };



    private  static String[] listOfCities = {"Moscow",
            "Saint-Petersburg",
                    "Warsaw",
                    "New York",
                    "Sydney",
                    "Tokyo"};

   static final int STATUS_OK = 777;
   static final int STATUS_FAIL = 666;

   public WeatherInfo(){ }

       public WeatherInfo(String cityName,String cityTemp,String cityHumidity,String cityPressure,String cityWindSpeed, String[] cityTempFiveDays,String[] cityHumFiveDays){
            this.cityName = cityName;
            this.cityTemp = cityTemp;
            this.cityHumidity = cityHumidity;
            this.cityPressure = cityPressure;
            this.cityWindSpeed = cityWindSpeed;
            this.cityTempFiveDays = cityTempFiveDays;
            this.cityHumFiveDays = cityHumFiveDays;
   }


        public String getCityName(){
           return cityName;
        }
       public String getCityTemp(){
           return cityTemp;
       }
       public String getCityHumidity(){
           return cityHumidity;
       }
       public String getCityPressure(){
           return cityPressure;
       }
       public String getCityWindSpeed(){
           return cityWindSpeed;
       }
       public String[] getCityTempFiveDays(){
           return cityTempFiveDays;
       }
       public String[] getCityHumFiveDays(){
           return cityHumFiveDays;
       }

       public String [] getCityNamesArray() {return cityNamesArray; }
       public String [] getWeatherArray() {return weatherArray; }
       public String [] getTemperatureArray() {return temperatureArray; }
       public String [] getHumidityArray() {return humidityArray; }
        public String [] getPressureArray() {return pressureArray; }
        public String [] getWindArray() {return windArray; }
        public String [] getListOfCities() {return listOfCities; }
        public Integer [] getImagesArray() {return imagesArray; }
}
