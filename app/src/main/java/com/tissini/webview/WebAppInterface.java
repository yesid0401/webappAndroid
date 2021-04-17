package com.tissini.webview;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;


public class WebAppInterface {
    Context mContext;
    Activity activity;
    /** Instantiate the interface and set the context */
    public WebAppInterface(Context c,Activity a) {
        mContext = c;
        this.activity = a;
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

    @JavascriptInterface
    public void closeApp(){
        this.activity.finish();
    }

    @JavascriptInterface
    public void  updateApp(){
        System.out.println("VAMOS ACTUALIZAR");
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.tissini.webview"));
            this.activity.startActivity(intent);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
