package com.tissini.webview.controllers;
import android.content.Context;

import com.tissini.webview.models.ManagerPreference;
import com.tissini.webview.services.NotifificationServices;
import java.util.ArrayList;
import static com.tissini.webview.helpers.Functions.ParserDataLocalStorage;

public class NotificationController {

    private static NotifificationServices notifificationServices = new NotifificationServices();

    public static void readNotification(String idNotification,String option, Context context){
            String idClient = new ManagerPreference(context).getUserPreference();
            if(idClient != ""){
                notifificationServices.readNotification(idClient, idNotification,option);
            }
    }
}
