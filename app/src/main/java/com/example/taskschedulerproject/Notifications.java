package com.example.taskschedulerproject;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

public class Notifications extends Application {
    public static final String  Channel_1_ID = "channel_1";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            NotificationChannel channel_1 = new NotificationChannel(
              Channel_1_ID,"Channel_1", NotificationManager.IMPORTANCE_HIGH
            );
            channel_1.setDescription("This is channel 1");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel_1);
        }
    }
}
