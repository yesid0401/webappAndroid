package com.tissini.webview.controllers;

import com.tissini.webview.services.NotifificationServices;

public class NotificationController {

    private static NotifificationServices notifificationServices = new NotifificationServices();

    public static void readNotification(String idClient,String idNotification ){
        notifificationServices.readNotification(idClient,idNotification);
    }
}
