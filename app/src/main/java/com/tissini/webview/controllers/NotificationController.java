package com.tissini.webview.controllers;

import com.tissini.webview.services.NotifificationServices;
import static com.tissini.webview.helpers.Functions.ParserData;

public class NotificationController {

    private static NotifificationServices notifificationServices = new NotifificationServices();

    public static void readNotification(String idNotification,String value){
        String[] values = ParserData(value);
        String idClient = values[0];
        notifificationServices.readNotification(idClient,idNotification);
    }
}
