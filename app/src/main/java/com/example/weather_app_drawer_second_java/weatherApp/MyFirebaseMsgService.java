package com.example.weather_app_drawer_second_java.weatherApp;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.weather_app_drawer_second_java.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMsgService extends FirebaseMessagingService {
    private int messageId = 0;
    public MyFirebaseMsgService() {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d("Push from myfire msg",remoteMessage.getNotification().getTitle());
        String title = remoteMessage.getNotification().getTitle();
        if(title == null){
            title = "Push title msg";
        }
        String text = remoteMessage.getNotification().getBody();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"2").
                setSmallIcon(R.mipmap.ic_launcher).
                setContentTitle(title).
                setContentText(text);
        NotificationManager notificationManager =(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId,builder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
      Log.d("Push msg from" + getClass().getName(), "Token" + s);
      sendTokenToServer(s);
    }
    private void sendTokenToServer(String token){

    }
}
