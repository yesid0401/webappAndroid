package com.tissini.webview.helpers;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tissini.webview.BuildConfig;
import com.tissini.webview.MainActivity;
import com.tissini.webview.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Functions {
    private static final int STORAGE_PERMISSION = 1000;
    private static String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final static  String CHANEL_ID = "tissini";
    private final static  String CHANEL_ID_NAME = "tissini";

    public static void goToThePlayStore(Context mContext){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID));
            mContext.startActivity(intent);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void openApplication(String link,String packages,Context mContext){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setPackage(packages);
        i.setData(Uri.parse(link));
        mContext.startActivity(i);
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
        arrayList.add("921010");
        arrayList.add(user_stage);
        arrayList.add(user_escalafon);
        arrayList.add("Yesid prueba");

        return arrayList;
    }

    public  static  HashMap<String,String> listImagesLinks(String value){
        HashMap<String,String> linkImages = new HashMap<>();
        JsonArray arrayJsonImages = (JsonArray) JsonParser.parseString(value);

        for (int i = 0;i < arrayJsonImages.size();i++){
            JsonObject jsonObject = JsonParser.parseString(arrayJsonImages.get(i).toString()).getAsJsonObject();
            String image = jsonObject.get("image").toString();
            String link = jsonObject.get("link").toString();
            linkImages.put(image,link);
        }

        return linkImages;
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

    public static Bitmap getBitmapFromURL(String src) {
        try {
            src = src.replaceAll("^[\"']+|[\"']+$", "");
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            connection.disconnect();
            return bitmap;
        }
        catch (IOException e) {
            System.out.println("error al obtener imagen 1 " +e);
            return null;
        }

    }

    public static void shareImage(Bitmap img,String imageName,String productName,String productURL,Context mContext) throws IOException {
        File file = new File(mContext.getExternalCacheDir(), "Compartir "+imageName);
        FileOutputStream fOut = new FileOutputStream(file);
        img.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        fOut.flush();
        fOut.close();
        file.setReadable(true, false);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
        intent.putExtra(Intent.EXTRA_TEXT,productName + "\n"+productURL);
        intent.setType("image/png");
        mContext.startActivity(Intent.createChooser(intent,imageName));
    }

    public static void downloadImage(String url,String title,Context mContext) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setTitle(title);
                request.setDescription("tissini.app");
                request.allowScanningByMediaScanner();
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "TISSINI/" + title);
                DownloadManager manager = (DownloadManager) mContext.getSystemService(mContext.DOWNLOAD_SERVICE);
                manager.enqueue(request);

                Toast.makeText(mContext, "Iniciando descarga", Toast.LENGTH_LONG).show();
                Toast.makeText(mContext, "Descarga finalizada", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions((Activity) mContext, permissions, STORAGE_PERMISSION);
            }

        }
    }

    public static void NotificationUpdate(Context mContext){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANEL_ID,CHANEL_ID_NAME, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Bitmap img = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.update);
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra("updateAppInPlayStore","updateAppInPlayStore");
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,1,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        String message ="Presiona en el botón Actualizar para que disfrutes de nuevas y mejores experiencias";

        NotificationCompat.Builder notification = new NotificationCompat.Builder(mContext.getApplicationContext(),CHANEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("¡Vamos a actualizar tu oficina virtual!")
                .setContentText(message)
                .setColor(Color.parseColor("#FF4EF2"))
                .addAction(R.mipmap.ic_launcher,"Actualizar",pendingIntent)
                .setFullScreenIntent(pendingIntent,true)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(img))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext.getApplicationContext());
        notificationManagerCompat.notify(0,notification.build());

    }

    public static PendingIntent getPendingIntent(String idNotification,String action, Class Receiver,Context context){
        Intent intent = new Intent(context, Receiver);
        Bundle bundle = new Bundle();
        bundle.putString("idNotification",idNotification);
        bundle.putString("action",action);
        intent.putExtras(bundle);
        PendingIntent leftArrowPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return  leftArrowPendingIntent;
    }

}
