package com.tissini.webview.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.fcm.MessagingService;
import com.tissini.webview.MainActivity;
import com.tissini.webview.R;
import static com.tissini.webview.helpers.Functions.getBitmapFromURL;


public class NotificationsMessagingService extends MessagingService {
    private final static  String CHANEL_ID = "tissini";
    private final static  String CHANEL_ID_NAME = "tissini";
    private final static  String GROUP_KEY_WORK_APP = "com.tissini.app/notifications";
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
        String image          = remoteMessage.getData().get("image");

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(),CHANEL_ID);

        if(!image.equals("")){
            this.createNotificationWithImage(title,body,image,notification);
        }else{
            this.createNotificationWithoutImage(title,body,notification);
        }

        NotificationCompat.Builder summaryNotification =new NotificationCompat.Builder(getApplicationContext(),CHANEL_ID);
        this.createGroupedNotification(title,body,summaryNotification);
        this.createPendingIntent(link,idNotification,notification,summaryNotification);
    }

    public void createNotificationWithImage(String title,String body, String image, NotificationCompat.Builder notification){
        Bitmap img = getBitmapFromURL(image);
        notification
          .setSmallIcon(R.mipmap.ic_launcher)
          .setContentTitle(title)
          .setContentText(body)
          .setColor(Color.parseColor("#FF4EF2"))
          .setLargeIcon(img)
          .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(img).bigLargeIcon(null))
          .setPriority(NotificationCompat.PRIORITY_HIGH)
          .setGroup(GROUP_KEY_WORK_APP)
          .setAutoCancel(true);
    }

    public void createNotificationWithoutImage(String title,String body, NotificationCompat.Builder notification){
        notification
          .setSmallIcon(R.mipmap.ic_launcher)
          .setContentTitle(title)
          .setContentText(body)
          .setColor(Color.parseColor("#FF4EF2"))
          .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
          .setPriority(NotificationCompat.PRIORITY_HIGH)
          .setGroup(GROUP_KEY_WORK_APP)
          .setAutoCancel(true);
    }

    public void createGroupedNotification(String title,String body, NotificationCompat.Builder summaryNotification){
        summaryNotification
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setColor(Color.parseColor("#FF4EF2"))
            .setGroup(GROUP_KEY_WORK_APP)
            .setGroupSummary(true)
            .setAutoCancel(true);
    }

    public void createPendingIntent(String link,String idNotification, NotificationCompat.Builder notification, NotificationCompat.Builder summaryNotification){
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
