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

        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){

                //mywebViewClient.readNotification("158966","34");
                webView.post(new Runnable() {
                        @Override
                        public void run() {
                            webView.evaluateJavascript("localStorage.getItem('token')", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                    String token  =  (String) value.toString().replaceAll("^[\"']+|[\"']+$", "");
                                    String status = mywebViewClient.getJsonPlaceHolder(token);
                                    System.out.println("STATUS => "+status);
                                    if(status == "null"){
                                        System.out.println("STATUS => "+status);
                                    }

                                }
                            });
                        }
                    });
            }
        },0,20000);
    }
}
