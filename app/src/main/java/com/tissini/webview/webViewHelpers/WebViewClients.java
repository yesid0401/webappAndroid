package com.tissini.webview.webViewHelpers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.tissini.webview.controllers.VersionController;

import static com.tissini.webview.controllers.InterestController.addInterestsToUser;
import static com.tissini.webview.controllers.InterestController.saveInterestsInDataBase;
import static com.tissini.webview.controllers.NotificationController.readNotification;

public class WebViewClients extends WebViewClient {
    Activity activity;
    WebView webView;
    ProgressBar progressBar;
    Intent intent;
    VersionController versionController;

    public WebViewClients(ProgressBar progressBar, WebView webView, Activity activity, Intent intent){
        this.progressBar = progressBar;
        this.webView = webView;
        this.activity = activity;
        this.intent = intent;
        this.versionController  = new VersionController(this.webView);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView wv,  WebResourceRequest request) {
        String url = request.getUrl().toString();

        if(url.startsWith("whatsapp:") ||
                url.startsWith("tel:") ||
                url.startsWith("intent://") ||
                url.startsWith("http://") ||
                url.startsWith("https://io.tissini.app") ||
                url.startsWith("https://stage.tissini.app") ||
                url.startsWith("https://tissini.com/blog/")){
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                activity.startActivity(intent);
                if(!url.startsWith("https://io.tissini.app") && !url.startsWith("https://stage.tissini.app"))
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
        versionController.getVerion();
        webView.evaluateJavascript("JSON.parse(localStorage.getItem('customer'))", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                addInterestsToUser(value);
                saveInterestsInDataBase(value);
                String idNotification = intent.getStringExtra("idNotification");
                if(idNotification != null) {
                    readNotification(idNotification,value);
                }
          }
        });
    }

    @Override
    public void onPageStarted (WebView view, String url, Bitmap favicon){
        progressBar.setVisibility(View.VISIBLE);
    }
}
