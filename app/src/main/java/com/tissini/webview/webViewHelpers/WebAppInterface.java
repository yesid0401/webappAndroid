package com.tissini.webview.webViewHelpers;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import com.tissini.webview.BuildConfig;
import com.tissini.webview.helpers.BitmapH;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebAppInterface {
    Context mContext;
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
    public void optionImage(String title,String url,String option) throws IOException {

        if(option.equals("share")){
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            Bitmap img = BitmapH.getBitmapFromURL(url);
            shareImage(img,title);
        }

        if(option.equals("download")){
            downloadImage(url,title);
        }

    }


    @JavascriptInterface
    public void  updateApp(){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID));
            mContext.startActivity(intent);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
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
    public void shareImage(Bitmap img,String title) throws IOException {
        File file = new File(mContext.getExternalCacheDir(), "Compartir "+title);
        FileOutputStream fOut = new FileOutputStream(file);
        img.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        fOut.flush();
        fOut.close();
        file.setReadable(true, false);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
        intent.setType("image/png");
        mContext.startActivity(Intent.createChooser(intent,title));

    }

    /**
      url  : image link
     title : image name
     **/
    public void downloadImage(String url,String title){
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setTitle(title);
            request.setDescription("tissini.app");
            request.allowScanningByMediaScanner();
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,"TISSINI/"+title);
            DownloadManager manager = (DownloadManager) mContext.getSystemService(mContext.DOWNLOAD_SERVICE);
            manager.enqueue(request);

            Toast.makeText(mContext,"Descarga finalizada",Toast.LENGTH_LONG).show();
    }


}
