package com.tissini.webview.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.fcm.MessagingService;
import com.tissini.webview.MainActivity;
import com.tissini.webview.R;
import com.tissini.webview.helpers.LoadImageTask;
import com.tissini.webview.receivers.LeftBroadCastReceiver;
import com.tissini.webview.receivers.NotificationBroadCastReceiver;
import com.tissini.webview.receivers.RightBroadCastReceiver;

import java.util.HashMap;

import static com.tissini.webview.controllers.NotificationController.readNotification;
import static com.tissini.webview.helpers.Functions.getBitmapFromURL;
import static com.tissini.webview.helpers.Functions.getPendingIntent;
import static com.tissini.webview.helpers.Functions.listImagesLinks;


public class NotificationsMessagingService extends MessagingService {
    private final static  String CHANEL_ID = "tissini";
    private final static  String CHANEL_ID_NAME = "tissini";
    private final static  String GROUP_KEY_WORK_APP = "com.tissini.app/notifications";
    private static NotifificationServices notifificationServices = new NotifificationServices();

    public static NotificationManagerCompat notificationManager;
    public static  NotificationCompat.Builder notificationBuilder;
    public  static  RemoteViews remoteViews;

    @Override
    public void onMessageReceived( RemoteMessage remoteMessage) {
           createNotificationChanel();
           String typeNotification = remoteMessage.getData().get("typeNotification");
           if(typeNotification.equals("simple")) CreateNotification(remoteMessage);
           else creteNotificationWithScroll(remoteMessage);

  //
//
//           String idNotification = remoteMessage.getData().get("idNotification");
//           readNotification(idNotification,"delivered",this);
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


        if(!link.isEmpty()){
            if(!image.isEmpty()){
                this.createNotificationWithImage(title,body,image,notification);
            }else{
                this.createNotificationWithoutImage(title,body,notification);
            }

            NotificationCompat.Builder summaryNotification =new NotificationCompat.Builder(getApplicationContext(),CHANEL_ID);
            this.createGroupedNotification(title,body,summaryNotification);
            this.createPendingIntent(link,idNotification,image,notification,summaryNotification);
        }else{

            Intent intent = new Intent(this, NotificationBroadCastReceiver.class);
            intent.putExtra("idNotification",idNotification);
            intent.putExtra("Link","Nolink");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            if(!image.isEmpty()){
                this.createNotificationWithoutLinkWithImage(title,body,image,notification,pendingIntent);
            }else{
                this.createNotificationWithoutLinkWithoutImage(title,body,notification,pendingIntent);
            }
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(Integer.parseInt(idNotification),notification.build());
        }

    }

    public void createNotificationWithImage(String title,String body, String image, NotificationCompat.Builder notification){
        Bitmap img = getBitmapFromURL(image);
        notification
          .setSmallIcon(R.mipmap.ic_launcher)
          .setContentTitle(title)
          .setContentText(body)
          .setColor(Color.parseColor("#FF4EF2"))
          .setPriority(NotificationCompat.PRIORITY_HIGH)
          .setGroup(GROUP_KEY_WORK_APP)
          .setLargeIcon(img)
          .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(img).bigLargeIcon(null))
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

    public void createNotificationWithoutLinkWithImage(String title,String body, String image, NotificationCompat.Builder notification, PendingIntent pendingIntent){
        Bitmap img = getBitmapFromURL(image);
        notification
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setColor(Color.parseColor("#FF4EF2"))
                .setLargeIcon(img)
                .addAction(R.mipmap.ic_launcher,"Cerrar",pendingIntent)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(img).bigLargeIcon(null))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup(GROUP_KEY_WORK_APP)
                .setAutoCancel(true);
    }

    public void createNotificationWithoutLinkWithoutImage(String title,String body, NotificationCompat.Builder notification, PendingIntent pendingIntent){
        notification
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setColor(Color.parseColor("#FF4EF2"))
                .addAction(R.mipmap.ic_launcher,"Cerrar",pendingIntent)
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

    public void createPendingIntent(String link,String idNotification, String image,NotificationCompat.Builder notification, NotificationCompat.Builder summaryNotification){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("link",link);
        intent.putExtra("idNotification",idNotification);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, Integer.parseInt(idNotification),intent,PendingIntent.FLAG_ONE_SHOT);
        notification.setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(Integer.parseInt(idNotification),notification.build());
        notificationManagerCompat.notify(0,summaryNotification.build());

//        LoadImageTask currentLoadImageTask = new LoadImageTask(notificationManagerCompat, notification);
//
//        currentLoadImageTask.execute(image);
    }


    public void creteNotificationWithScroll(RemoteMessage remoteMessage){
        String body           = remoteMessage.getData().get("body");
        String title          = remoteMessage.getData().get("title");
        String link           = remoteMessage.getData().get("link");
        String idNotification = remoteMessage.getData().get("idNotification");
        String linksAndImages = remoteMessage.getData().get("data");

        HashMap<String,String> linkImages = listImagesLinks(linksAndImages);

        remoteViews = new RemoteViews(this.getPackageName(),R.layout.carousel_notification );
        remoteViews.setTextViewText(R.id.tvCarousalTitle,title);
        remoteViews.setTextViewText(R.id.tvCarousalContent,body);


        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("link",link);
        intent.putExtra("idNotification",idNotification);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, Integer.parseInt(idNotification),intent,PendingIntent.FLAG_UPDATE_CURRENT);


        PendingIntent leftArrowPendingIntent = getPendingIntent(idNotification,"previews",LeftBroadCastReceiver.class,this);
        remoteViews.setOnClickPendingIntent(R.id.ivArrowLeft, leftArrowPendingIntent);

        PendingIntent rightArrowPendingIntent = getPendingIntent(idNotification,"next",RightBroadCastReceiver.class,this);
        remoteViews.setOnClickPendingIntent(R.id.ivArrowRight, rightArrowPendingIntent);


        RemoteViews remoteViewImage;

        for (String value : linkImages.keySet()) {
            try {
                Bitmap img = getBitmapFromURL(value);

                remoteViewImage = new RemoteViews(this.getPackageName(),R.layout.slider_item );
                remoteViewImage.setImageViewBitmap(R.id.ivImage, img);
                remoteViewImage.setTextViewText(R.id.tvi,linkImages.get(value));
                remoteViews.addView(R.id.viewFlipper,remoteViewImage);

                remoteViewImage.setOnClickPendingIntent(R.id.ivImage,pendingIntent);
            }catch (Exception e){
                System.out.println("error => "+e);
            }

        }
        notificationBuilder = new NotificationCompat.Builder(this.getApplicationContext(),CHANEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(Color.parseColor("#FF4EF2"))
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomBigContentView(remoteViews)
                 .setSound(null)
                .setAutoCancel(true);

        notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager = NotificationManagerCompat.from(this.getApplicationContext());
        notificationManager.notify(Integer.parseInt(idNotification),notificationBuilder.build());

    }





}
