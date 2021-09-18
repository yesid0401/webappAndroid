package com.tissini.webview;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.tissini.webview.services.NotificationBroadCastReceiver;


public class CarouselActivity extends AppCompatActivity {
  //  SliderView sliderView;
    private final static  String CHANEL_ID = "tissini";
    private final static  String CHANEL_ID_NAME = "tissini";
    public ViewFlipper viewFlipper;
    public static RemoteViews remoteViewsFlipper;
    public static  NotificationManagerCompat notificationManagerCompat;
    public  static    NotificationCompat.Builder notification;
    int[] images = {R.drawable.one,
            R.drawable.two,
            R.drawable.three,
            R.drawable.four,
            R.drawable.five,
            R.drawable.six};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.carousel_notification);
//
         // viewFlipper = findViewById(R.id.viewFlipper);
//        imageView = (ImageView)findViewById(R.id.ivArrowLeft);
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("clickkkkkkk");
//                viewFlipper.showPrevious();
//            }
//        });

        NotificationUpdate(getApplicationContext(),this.images);

    }


    public static void NotificationUpdate(Context mContext,int[] images){


        remoteViewsFlipper = new RemoteViews(mContext.getPackageName(),R.layout.carousel_notification );


        Intent intent_ivArrowLeft = new Intent(mContext, NotificationBroadCastReceiver.class);
        intent_ivArrowLeft.putExtra("ivArrowLeft","ivArrowLeft");
        PendingIntent pendingIntent_ivArrowLeft = PendingIntent.getBroadcast(mContext,0,intent_ivArrowLeft,PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViewsFlipper.setOnClickPendingIntent(R.id.ivArrowLeft,pendingIntent_ivArrowLeft);

        //////////////////////////////////////////////////////////////

        Intent intent_ivArrowRight = new Intent(mContext, NotificationBroadCastReceiver.class);
        intent_ivArrowLeft.setClassName(mContext.getPackageName(),"NotificationBroadCastReceiver");
        intent_ivArrowRight.putExtra("ivArrowRight","ivArrowRight");
        PendingIntent pendingIntent_ivArrowRight = PendingIntent.getBroadcast(mContext,1,intent_ivArrowRight,PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViewsFlipper.setOnClickPendingIntent(R.id.ivArrowRight,pendingIntent_ivArrowRight);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANEL_ID,CHANEL_ID_NAME, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        remoteViewsFlipper.setTextViewText(R.id.tvCarousalTitle,"¡Vamos a actualizar tu oficina virtual!");
        remoteViewsFlipper.setTextViewText(R.id.tvCarousalContent,"Presiona en el botón Actualizar para que disfrutes de nuevas y mejores experiencias");

        remoteViewsFlipper.setRemoteAdapter(R.id.viewFlipper,intent_ivArrowLeft);

        for(int i = 0;i < images.length;i++){
            RemoteViews remoteViewImage = new RemoteViews(mContext.getPackageName(),R.layout.slider_item );
            remoteViewImage.setImageViewResource(R.id.ivImage,images[i]);
            remoteViewsFlipper.addView(R.id.viewFlipper,remoteViewImage);

        }

        String message ="Presiona en el botón Actualizar para que disfrutes de nuevas y mejores experiencias";

        notification = new NotificationCompat.Builder(mContext.getApplicationContext(),CHANEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(Color.parseColor("#FF4EF2"))
                .setContentTitle("¡Vamos a actualizar tu oficina virtual!")
                .setContentText(message)
                .setCustomBigContentView(remoteViewsFlipper)
                .setAutoCancel(true);

        notificationManagerCompat = NotificationManagerCompat.from(mContext.getApplicationContext());
        notificationManagerCompat.notify(0,notification.build());

    }


}
