package com.example.weather_app_drawer_second_java;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.weather_app_drawer_second_java.weatherApp.JsonCurrentClass.Example;
import com.example.weather_app_drawer_second_java.weatherApp.JsonForecastClasses.Example3;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NotificationWeatherIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_RAIN = "com.example.weather_app_drawer_second_java.action.ACTION_RAIN";
    private static final String EXTRA_RAINNOTIFIER = "com.example.weather_app_drawer_second_java.extra.RAINNOTIFIER";

    private final String weatherSite = "https://api.openweathermap.org/data/2.5/forecast?q=";
    private final String apiKey = "&appid=80b8b51878e4ae64fc72d800c1679d04";
    private final String CITY = "Moscow";       //в качестве заглушки пока один город, потом будет последний выбранный или из любымых
    private int seconds = 10;                     //10секунд для теста.
    private final String NOTIFICATION = "It's about to rain";
    private final String APP_NAME = "Cosy weather app: ";
    private final String ALERT_WEATHER_TYPE = "Rain";


    public NotificationWeatherIntentService() {
        super("NotificationWeatherIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1) {
        Intent intent = new Intent(context, NotificationWeatherIntentService.class);
        intent.setAction(ACTION_RAIN);
        intent.putExtra(EXTRA_RAINNOTIFIER, param1);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_RAIN.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_RAINNOTIFIER);
                delay(seconds);
            }
        }
    }

    private void delay(int seconds) {
        long result = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, seconds);
        Date finishTime = calendar.getTime();
        while (Calendar.getInstance().getTime().before(finishTime)) {
            if (result == Long.MAX_VALUE) {
                result = 0;
            }
            result++;
        }
        if (checkRainInBd()) {
            handleActionFoo(NOTIFICATION);
        }
    }

    private void handleActionFoo(String param1) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "2")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(APP_NAME)
                .setContentText(param1);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(2, builder.build());
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent intent = new Intent(getApplicationContext(), this.getClass());
        intent.setPackage(getPackageName());
        startService(intent);
    }


    private boolean checkRainInBd() {
        String url2 = weatherSite.concat(CITY).concat(apiKey);

        try {
            final URL url = new URL(url2);
            HttpsURLConnection urlConnection = null;
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);

            int code = urlConnection.getResponseCode();
            if (code >= 200 && code <= 299) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String result = null;
                if ((result = in.readLine()) != null) {

                }
                Example3 data2 = new Gson().fromJson(result, Example3.class);
                for (int i = 0; i <= 5; i++) {
                    String temp = data2.getList().get(i).getWeather().get(0).getMain();
                    if (temp.contains(ALERT_WEATHER_TYPE)) {
                        return true;
                    }
                }
                return false;
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}