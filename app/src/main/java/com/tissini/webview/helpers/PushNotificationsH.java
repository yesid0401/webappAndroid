package com.tissini.webview.helpers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pusher.pushnotifications.PushNotifications;

public class PushNotificationsH {


    public static void addInterest(String value){
        if(!value.equals("null")){

            String[] values = Parser(value);

            String user_id         = values[0];
            String user_stage      = values[1];
            String user_escalafon  = values[2];

            PushNotifications.addDeviceInterest("general");
            PushNotifications.addDeviceInterest(user_id);
            PushNotifications.addDeviceInterest("Login");
            PushNotifications.addDeviceInterest(user_stage);
            PushNotifications.removeDeviceInterest("noLogin");

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
        JsonElement stage     = jsonObject.get("stage");
        JsonElement elite     = jsonObject.get("elite");

        JsonElement jsonTree2  = parser.parse(String.valueOf(elite));
        JsonObject jsonObject2 = jsonTree2.getAsJsonObject();
        JsonElement escalafon  = jsonObject2.get("escalafon");

        String user_id         = (String) id.toString();
        String user_stage      = (String) stage.toString().replaceAll("^[\"']+|[\"']+$", "");
        String user_escalafon  = (String) escalafon.toString();

        String[] values = new String[3];

        values[0] = user_id;
        values[1] = user_stage;
        values[2] = user_escalafon;

        return values;
    }


}
