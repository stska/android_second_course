package com.example.weather_app_drawer_second_java.weatherApp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.ConditionVariable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.weather_app_drawer_second_java.R;

public class SystemMessageReceiver extends BroadcastReceiver {
    private int messageId = 0;
    private boolean isWifiOn = false;
    private boolean isMobOn = false;
    private final String ENABLED = "enabled";
    private final String DISABLED = "disabled";

    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = "";
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (connectivityManager != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (networkCapabilities != null) {
                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        isWifiOn = true;
                        msg = "Wifi status is " + ENABLED;
                    } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        isMobOn = true;
                        msg = "Mobile network status is " + ENABLED;
                    } else {
                        msg = "Turn on the internet. Currently it's " + DISABLED;
                    }
                }
            }

        } else {
            if (connectivityManager != null) {
                for (Network network : connectivityManager.getAllNetworks()) {
                    NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        isWifiOn |= networkInfo.isConnected();
                        msg = "Wifi status is " + ENABLED;
                    } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        isMobOn |= networkInfo.isConnected();
                        msg = " Mobile network status is " + ENABLED;
                    } else {
                        msg = "Turn on the internet. Currently it's " + DISABLED;
                    }
                }
            }
        }
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(context, "2").
                setSmallIcon(R.mipmap.ic_launcher).
                setContentTitle("Weather app: ").
                setContentText(msg);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, notifBuilder.build());

    }

    public boolean getIsWifiOn() {
        return isWifiOn;
    }

    public boolean getIsMobOn() {
        return isMobOn;
    }

}

