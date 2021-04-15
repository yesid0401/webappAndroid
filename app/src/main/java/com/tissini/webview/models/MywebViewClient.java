package com.tissini.webview.models;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pusher.pushnotifications.PushNotifications;


public class MywebViewClient extends WebViewClient {

    Activity activity;
    WebView webView;
    ProgressBar progressBar;
    Intent intent;

    public MywebViewClient(ProgressBar progressBar, WebView webView, Activity activity,Intent intent){
        this.progressBar = progressBar;
        this.webView = webView;
        this.activity = activity;
        this.intent = intent;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView wv, String url) {
        if(url.startsWith("whatsapp:") || url.startsWith("tel:") || url.startsWith("intent://") || url.startsWith("http://") || url.startsWith("https://io.tissini")) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                activity.startActivity(intent);
                if(!url.startsWith("https://io.tissini"))
                   webView.goBack();
                return true;
            }catch (android.content.ActivityNotFoundException e){
                System.out.println("Error with " + url + ": " + e.toString());
            }

        }
        return false;
    }

    @Override
    public void onPageFinished (WebView view,String url){
        progressBar.setVisibility(View.INVISIBLE);
        webView.evaluateJavascript("JSON.parse(localStorage.getItem('customer'))", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                addInteres(value);
            }
        });
    }

    @Override
    public void onPageStarted (WebView view, String url, Bitmap favicon){
        progressBar.setVisibility(View.VISIBLE);
    }

    /// Methods

    public void addInteres(String value){
        if(!value.equals("null")){
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
}
