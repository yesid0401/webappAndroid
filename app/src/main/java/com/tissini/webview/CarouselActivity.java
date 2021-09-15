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
import android.widget.RemoteViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class CarouselActivity extends AppCompatActivity {
  //  SliderView sliderView;
    private final static  String CHANEL_ID = "tissini";
    private final static  String CHANEL_ID_NAME = "tissini";

    //Button button;

    int[] images = {R.drawable.one,
            R.drawable.two,
            R.drawable.three,
            R.drawable.four,
            R.drawable.five,
            R.drawable.six};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.carousel_notification);

        //button = (Button) findViewById(R.id.idButton);

//        sliderView = findViewById(R.id.image_slider);
//
//        SliderAdapter sliderAdapter = new SliderAdapter(this.images);
//
//        sliderView.setSliderAdapter(sliderAdapter);
//        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
//        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
//        sliderView.startAutoCycle();

        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(),R.layout.carousel_notification );
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               NotificationUpdate(getApplicationContext(),remoteViews);
//            }
//        });
        NotificationUpdate(getApplicationContext(),remoteViews);
    }


    public static void NotificationUpdate(Context mContext, RemoteViews remote){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANEL_ID,CHANEL_ID_NAME, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Bitmap img = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.update);
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra("updateAppInPlayStore","updateAppInPlayStore");
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,1,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        String message ="Presiona en el botón Actualizar para que disfrutes de nuevas y mejores experiencias";

        NotificationCompat.Builder notification = new NotificationCompat.Builder(mContext.getApplicationContext(),CHANEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("¡Vamos a actualizar tu oficina virtual!")
                .setContentText(message)
                .setCustomBigContentView(remote)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext.getApplicationContext());
        notificationManagerCompat.notify(0,notification.build());

    }


}
