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
import java.util.HashMap;
import java.util.Map;

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

        JsonObject customer_object    = JsonParser.parseString(value).getAsJsonObject();
        String user_id                = customer_object.get("id").toString();
        String user_name              = customer_object.get("name").toString().replaceAll("^[\"']+|[\"']+$", "");
        String user_stage             = customer_object.get("stage").toString().replaceAll("^[\"']+|[\"']+$", "");
        JsonElement elite             = customer_object.get("elite");

        JsonObject elite_object   = JsonParser.parseString(String.valueOf(elite)).getAsJsonObject();
        String user_escalafon     = tranformEscalafon(elite_object.get("escalafon").toString());

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

    public static String tranformEscalafon(String user_escalafon){
        Map<String,String> hashMapEscalafon = new HashMap<String, String>();
        hashMapEscalafon.put("null","Perla");
        hashMapEscalafon.put("143464","Esmeralda");
        hashMapEscalafon.put("143462","Zafiro");
        hashMapEscalafon.put("143465","Rubi");
        hashMapEscalafon.put("143463","Diamante");
        hashMapEscalafon.put("236230","Estrella_Rosa");
        hashMapEscalafon.put("563468","Estrella_Dorada");
        return hashMapEscalafon.get(user_escalafon) != null ?  hashMapEscalafon.get(user_escalafon): "Perla";
    }

}
