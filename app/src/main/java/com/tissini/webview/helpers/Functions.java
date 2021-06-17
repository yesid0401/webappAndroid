package com.tissini.webview.helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tissini.webview.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Functions {


    public static void goToThePlayStore(Context mContext){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID));
            mContext.startActivity(intent);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public static String[] ParserData(String value){

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

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (IOException e) {
            return null;
        }
    }

}
