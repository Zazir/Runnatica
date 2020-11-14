package com.runnatica.runnatica.services;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import java.util.Collections;

public class App extends Application {
    public static final String CHANNEL_ID= "exampleServiceChannel1";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannels(Collections.singletonList(serviceChannel));
        }
    }
}
