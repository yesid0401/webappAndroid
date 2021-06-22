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
import java.util.ArrayList;

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

    public static ArrayList<String> ParserDataLocalStorage(String value){

        JsonObject jsonObject    = JsonParser.parseString(value).getAsJsonObject();
        JsonElement id           = jsonObject.get("id");
        JsonElement name         = jsonObject.get("name");
        JsonElement stage        = jsonObject.get("stage");
        JsonElement elite        = jsonObject.get("elite");

        JsonObject jsonObject2   = JsonParser.parseString(String.valueOf(elite)).getAsJsonObject();
        JsonElement escalafon    = jsonObject2.get("escalafon");

        String user_id           = id.toString();
        String user_name         = name.toString().replaceAll("^[\"']+|[\"']+$", "");
        String user_stage        = stage.toString().replaceAll("^[\"']+|[\"']+$", "");
        String user_escalafon    = escalafon.toString();

        ArrayList<String> arrayList = new ArrayList();
        arrayList.add(user_id);
        arrayList.add(user_stage);
        arrayList.add(user_escalafon);
        arrayList.add(user_name);

        return arrayList;
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
