package com.example.weather_app_drawer_second_java.weatherApp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;

public class NetworkCallableCheckUp {
    private final Context context;
    public static boolean isConnected = false;

    public NetworkCallableCheckUp(Context context){
        this.context = context;
    }
    public void signNetworkCall() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(@NonNull Network network) {
                        isConnected = true;
                    }

                    @Override
                    public void onLost(@NonNull Network network) {
                        isConnected = false;
                    }
                });
                isConnected = false;
            }
        }catch (Exception e){
            isConnected = false;
        }
    }
}
