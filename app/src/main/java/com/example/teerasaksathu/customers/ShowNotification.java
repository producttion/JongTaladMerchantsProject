package com.example.teerasaksathu.customers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by teerasaksathu on 9/22/2017 AD.
 */

public class ShowNotification {

   private Context context;

    public ShowNotification(Context context) {
        this.context = context;

    }


    public void showNotification() {

        Notification notification =
                new NotificationCompat.Builder(context) // this is context
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("แจ้งเตือน")
                        .setContentText("Data input")
                        .setAutoCancel(true)
                        .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1000, notification);
    }
}
