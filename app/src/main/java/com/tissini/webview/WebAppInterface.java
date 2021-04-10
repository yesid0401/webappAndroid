package com.tissini.webview;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tissini.webview.models.MywebViewClient;

import java.util.Timer;
import java.util.TimerTask;


public class WebAppInterface {
    Context mContext;
    WebView webView;
    MywebViewClient mywebViewClient;
    public String status;
    /** Instantiate the interface and set the context */
    public WebAppInterface(Context c,WebView webView) {
        mContext = c;
        this.webView = webView;
        mywebViewClient = new MywebViewClient();
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
    public void getCart(){
        getToken();
    }

    public void getToken(){
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.evaluateJavascript("localStorage.getItem('token')", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        String token  =  (String) value.toString().replaceAll("^[\"']+|[\"']+$", "");
                        getcart2(token);
                    }
                });
            }

        });
    }


    public void getcart2(String token){
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                status = mywebViewClient.getJsonPlaceHolder(token);
                if(status != null){

                    if(!status.equals("processing")){
                        this.cancel();
                    }

                }
                System.out.println("IMPRIMIENDO DESDE WEBAPPINTERFACE");
            }
        },1,10000);
    }
}
