package com.tissini.webview.controllers;
import com.tissini.webview.services.NotifificationServices;
import java.util.ArrayList;
import static com.tissini.webview.helpers.Functions.ParserDataLocalStorage;

public class NotificationController {

    private static NotifificationServices notifificationServices = new NotifificationServices();

    public static void readNotification(String idNotification,String value){
        if(!value.equals("null")) {
            ArrayList values = ParserDataLocalStorage(value);
            String idClient = values.get(0).toString();
            notifificationServices.readNotification(idClient, idNotification,"seen");
        }
    }
}
