package com.tissini.webview.services;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.tissini.webview.CarouselActivity;
import com.tissini.webview.R;

import static com.tissini.webview.CarouselActivity.notification;
import static com.tissini.webview.CarouselActivity.notificationManagerCompat;
import static com.tissini.webview.CarouselActivity.remoteViewsFlipper;

public class NotificationBroadCastReceiver  extends BroadcastReceiver {
    public  ViewFlipper viewFlipper;
    @Override
    public void onReceive(Context context, Intent intent) {
//        int idNotification = Integer.parseInt(String.valueOf(intent.getStringExtra("idNotification")));
//        System.out.println("idNotification => " + idNotification);
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.cancel(idNotification);


//        ((CarouselActivity.instance)).setContentView(R.layout.carousel_notification);
//        viewFlipper = (ViewFlipper)CarouselActivity.instance.findViewById(R.id.viewFlipper);





        String ivArrowLeft = intent.getStringExtra("ivArrowLeft");
        if(ivArrowLeft != null){
            System.out.println("Izquierda");
            remoteViewsFlipper.showPrevious(R.id.ivArrowLeft);
            Toast toast = Toast.makeText(context, "Ir atras", Toast.LENGTH_LONG);
            toast.show();


            System.out.println(remoteViewsFlipper.getPackage());
        }

        String ivArrowRight = intent.getStringExtra("ivArrowRight");
        if(ivArrowRight != null){
            System.out.println("ivArrowRight");
            //viewFlipper.showNext();
            remoteViewsFlipper.showNext(R.id.ivArrowRight);
            Toast toast = Toast.makeText(context, "Ir Adelante", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}

