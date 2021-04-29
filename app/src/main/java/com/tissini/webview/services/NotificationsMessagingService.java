package com.tissini.webview.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.fcm.MessagingService;
import com.tissini.webview.MainActivity;
import com.tissini.webview.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class NotificationsMessagingService extends MessagingService {
    private final static  String CHANEL_ID = "tissini";
    private final static  String CHANEL_ID_NAME = "tissini";
    @Override
    public void onMessageReceived( RemoteMessage remoteMessage) {
           createNotificationChanel();
           CreateNotification(remoteMessage);
    }

    public void createNotificationChanel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANEL_ID,CHANEL_ID_NAME, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void CreateNotification(RemoteMessage remoteMessage){
        String body           = remoteMessage.getData().get("body");
        String title          = remoteMessage.getData().get("title");
        String link           = remoteMessage.getData().get("link");
        String idNotification = remoteMessage.getData().get("idNotification");

        String GROUP_KEY_WORK_EMAIL = "com.tissini.app/notifications";
      //  Bitmap img = getBitmapFromURL("https://io.tissini.app/img/categories/textiles-ropa-interior-panties.png");

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(),CHANEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setColor(Color.parseColor("#FF4EF2"))
                //.setLargeIcon(img)
                //.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(img).bigLargeIcon(null))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup(GROUP_KEY_WORK_EMAIL)
                .setAutoCancel(true);

        NotificationCompat.Builder summaryNotification =new NotificationCompat.Builder(getApplicationContext(),CHANEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(Color.parseColor("#FF4EF2"))
                .setGroup(GROUP_KEY_WORK_EMAIL)
                .setGroupSummary(true)
                .setAutoCancel(true);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("link",link);
        intent.putExtra("idNotification",idNotification);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, Integer.parseInt(idNotification),intent,PendingIntent.FLAG_ONE_SHOT);
        notification.setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(Integer.parseInt(idNotification),notification.build());
        notificationManagerCompat.notify(0,summaryNotification.build());

    }
}
