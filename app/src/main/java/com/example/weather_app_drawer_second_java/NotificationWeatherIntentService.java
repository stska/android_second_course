package com.example.weather_app_drawer_second_java;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

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
public class NotificationWeatherIntentService extends Service {
    private static final String ACTION_RAIN = "com.example.weather_app_drawer_second_java.action.ACTION_RAIN";
    private static final String EXTRA_RAINNOTIFIER = "com.example.weather_app_drawer_second_java.extra.RAINNOTIFIER";
    private final String weatherSite = "https://api.openweathermap.org/data/2.5/forecast?q=";
    private final String apiKey = "&appid=80b8b51878e4ae64fc72d800c1679d04";
    private final String CITY = "Moscow";       //в качестве заглушки пока один город, потом будет последний выбранный или из любымых
    private int seconds = 10;                     //10секунд для теста.
    private final String NOTIFICATION = "It's about to rain";
    private final String APP_NAME = "Cosy weather app: ";
    private final String ALERT_WEATHER_TYPE = "Rain";
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            try {
                delay(seconds);
                Thread.sleep(5000);       //Значение 5000 просто для теста, значение будет равнятся часу, чтобы каждый час сервис проверял дождь на ближайщие три часа.
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("NewService", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void startActionFoo(Context context, String param1) {
        Intent intent = new Intent(context, NotificationWeatherIntentService.class);
        intent.setAction(ACTION_RAIN);
        intent.putExtra(EXTRA_RAINNOTIFIER, param1);
        context.startService(intent);
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