package com.tissini.webview.webViewHelpers;

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
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.tissini.webview.MainActivity;
import com.tissini.webview.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.tissini.webview.helpers.Functions.getBitmapFromURL;
import static com.tissini.webview.helpers.Functions.goToThePlayStore;

public class
WebAppInterface {
    private final static  String CHANEL_ID = "tissini";
    private final static  String CHANEL_ID_NAME = "tissini";
    Context mContext;
    private static final int STORAGE_PERMISSION = 1000;
    private String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /** Instantiate the interface and set the context */
    public WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void shareApi( String title,String body) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        mContext.startActivity(Intent.createChooser(intent, title));
    }

    /**
        title  : image name
        url    : image link
        option : option to perform => share o download
     **/
    @JavascriptInterface
    public void optionImage(String productName,String productURL,String imageName, String imageURL,String action) throws IOException {

        if(action.equals("share")){
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            Bitmap img = getBitmapFromURL(imageURL);
            shareImage(img,imageName,productName,productURL);
        }

        if(action.equals("download")){
            downloadImage(imageURL,imageName);
        }

    }


    @JavascriptInterface
    public void  updateApp(){
        createNotificationChanel();
        NotificationUpdate();
        goToThePlayStore(mContext);
    }

    @JavascriptInterface
    public void shareWhatsApp(String number,String body) throws UnsupportedEncodingException {
        number = "+"+number;
        String url = "https://api.whatsapp.com/send?phone="+ number +"&text=" + URLEncoder.encode(body, "UTF-8");
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setPackage("com.whatsapp");
        i.setData(Uri.parse(url));
        mContext.startActivity(i);
    }

    //METHODS

    /**
        img   : image to share
        title : image name
     **/
    public void shareImage(Bitmap img,String imageName,String productName,String productURL) throws IOException {

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

    /**
      url  : image link
     title : image name
     **/
    public void downloadImage(String url,String title) {

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

    public void createNotificationChanel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANEL_ID,CHANEL_ID_NAME, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public  void NotificationUpdate(){
        Bitmap img = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.update);
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

}
