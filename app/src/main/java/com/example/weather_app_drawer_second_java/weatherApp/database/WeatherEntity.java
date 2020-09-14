package com.example.weather_app_drawer_second_java.weatherApp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weather")
public class WeatherEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "city")
    public String cityName;

    @ColumnInfo(name = "country")
    public String countryName;

    @ColumnInfo(name = "weather_description")
    public String weatherDescriptionName;

    @ColumnInfo(name = "temperature")
    public String temperatureName;

    @ColumnInfo(name = "humidity")
    public String humidityName;

    @ColumnInfo(name = "pressure")
    public String pressureName;

    public String icon;

    public boolean favourite;

    //p.s я понимаю, что это никакая не третья форма и нужно разбивать таблицу на id старна, потом ид город и ид страны связанной с ним и затем уже таблицу для деталей о погоде.
    //но неуспеваю сделать, так как завтра уезжаб в коммандировку и пришлось сделать так, чтобы сдать дз. С течением времени, постараюсь привести бд, к разумному виду.
}
