package com.tissini.webview.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tissini.webview.R;

import static com.tissini.webview.services.NotificationsMessagingService.notificationBuilder;
import static com.tissini.webview.services.NotificationsMessagingService.notificationManager;
import static com.tissini.webview.services.NotificationsMessagingService.remoteViews;

public class LeftBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String action = bundle.getString("action");
        String notificationId = bundle.getString("idNotification");

        if(action.equals("previews")){
            remoteViews.showPrevious(R.id.viewFlipper);
            notificationManager.notify(Integer.parseInt(notificationId),notificationBuilder.build());
        }
    }
}
