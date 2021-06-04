package com.tissini.webview.helpers;

import android.content.Intent;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pusher.pushnotifications.PushNotifications;

import static com.tissini.webview.controllers.InterestController.createInterest;
import static com.tissini.webview.controllers.NotificationController.readNotification;

public class PushNotificationsH {

   public static Intent intent;



    public static void addInterest(String value){
        if(!value.equals("null")){

            String[] values = Parser(value);

            String user_id         = values[0];
            String user_stage      = values[1];
            String user_escalafon  = values[2];
            String user_name       = values[3];

            PushNotifications.clearDeviceInterests();
            PushNotifications.addDeviceInterest("general");
            PushNotifications.addDeviceInterest(user_id);
            PushNotifications.addDeviceInterest("Login");
            PushNotifications.addDeviceInterest(user_stage);
            PushNotifications.addDeviceInterest("Android");
            PushNotifications.removeDeviceInterest("noLogin");

            String client_id = user_id;
            String client_stage = user_stage;
            String client_name = user_name;

            createInterest(client_id,client_stage,client_name,"Android");

            String idNotification = intent.getStringExtra("idNotification");

            System.out.println("NOTIFICATION => "+idNotification);
            if (idNotification != null){
                String idClient = user_id;
                readNotification(idClient,idNotification);
            }

            System.out.println(PushNotifications.getDeviceInterests());

            if(!user_escalafon.equals("null"))
                PushNotifications.addDeviceInterest(user_escalafon);
        }else{
            PushNotifications.clearDeviceInterests();
            PushNotifications.addDeviceInterest("noLogin");
            System.out.println(PushNotifications.getDeviceInterests());
        }
    }


    public static String[] Parser(String value){

        JsonParser parser    = new JsonParser();
        JsonElement jsonTree = parser.parse(value);

        JsonObject jsonObject = jsonTree.getAsJsonObject();
        JsonElement id        = jsonObject.get("id");
        JsonElement name      = jsonObject.get("name");
        JsonElement stage     = jsonObject.get("stage");
        JsonElement elite     = jsonObject.get("elite");

        JsonElement jsonTree2  = parser.parse(String.valueOf(elite));
        JsonObject jsonObject2 = jsonTree2.getAsJsonObject();
        JsonElement escalafon  = jsonObject2.get("escalafon");

        String user_id         = (String) id.toString();
        String user_name       = (String) name.toString().replaceAll("^[\"']+|[\"']+$", "");
        String user_stage      = (String) stage.toString().replaceAll("^[\"']+|[\"']+$", "");
        String user_escalafon  = (String) escalafon.toString();

        String[] values = new String[4];

        values[0] = user_id;
        values[1] = user_stage;
        values[2] = user_escalafon;
        values[3] = user_name;

        return values;
    }


}
