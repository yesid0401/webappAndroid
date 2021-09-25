package com.tissini.webview.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int idNotification = Integer.parseInt(String.valueOf(intent.getStringExtra("idNotification")));
        System.out.println("idNotification => " + idNotification);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(idNotification);
    }
}

