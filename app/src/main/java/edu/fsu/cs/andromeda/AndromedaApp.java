package edu.fsu.cs.andromeda;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

import android.app.Application;
import android.app.NotificationManager;
import android.os.Build;

public class AndromedaApp extends Application {
    public static final String CHANNEL_1_ID = "edu.fsu.cs.andromeda.channel1";
    public static final String CHANNEL_2_ID = "channel2";

    @Override
    public void onCreate(){
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            android.app.NotificationChannel channel1 = new android.app.NotificationChannel(
                    CHANNEL_1_ID,
                    "Andromeda Network Notification",
                    IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1. It may have Network notifications.");

            android.app.NotificationChannel channel2 = new android.app.NotificationChannel(
                    CHANNEL_2_ID,
                    "Andromeda To Do Notification",
                    IMPORTANCE_HIGH
            );
            channel2.setDescription("This is Channel 2. It has to do notifications.");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }
}
