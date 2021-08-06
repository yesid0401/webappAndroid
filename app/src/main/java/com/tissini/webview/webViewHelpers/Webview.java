package com.tissini.webview.webViewHelpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import com.tissini.webview.BuildConfig;
import com.tissini.webview.R;

import java.util.HashMap;
import java.util.Map;

public class Webview {

    public WebView webView;
    public Activity activity;
    public WebSettings webSettings;
    public ProgressBar progressBar;
    public Intent intent;
    public String url_production;

    public Webview(Activity activity,Intent intent){
        this.activity           = activity;
        this.webView            = (WebView) activity.findViewById(R.id.webview);
        this.progressBar        = (ProgressBar) activity.findViewById(R.id.progressBar);
        this.intent             = intent;
        this.url_production     = activity.getString(R.string.production);
        this.webSettings        = this.webView.getSettings();
        this.webView.setWebChromeClient(new WebChromeClients(this.activity));
        this.webView.setWebViewClient(new WebViewClients(this.progressBar,this.webView,this.activity,intent));
        this.webView.addJavascriptInterface(new WebAppInterface(activity), "Webview");
    }

    public void webSettings(){
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(false);
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE  );
        webSettings.setUserAgentString(webSettings.getUserAgentString()+ " " + this.activity.getString(R.string.user_agent_suffix) + "" + BuildConfig.VERSION_NAME);
    }

    public void loadUrl(){
        webSettings();

        Uri appLinkData = intent.getData();
        if(appLinkData != null)
            this.url_production = appLinkData.toString(); //Open webview for deeplinking

        String link = intent.getStringExtra("link");
        if (link != null) {
            this.url_production = link; //Open webview for notification https://www.facebook.com/
            if (link.startsWith("https://www.facebook.com/") || link.startsWith("https://www.youtube.com/") || link.startsWith("https://us02web.zoom.us/") || link.startsWith("https://tissini.com")){
                this.url_production = activity.getString(R.string.production);
            }
        }


        Map<String, String> noCacheHeaders = new HashMap<String, String>(2);
        noCacheHeaders.put("Cache-Control", "no-cache");
        noCacheHeaders.put("Cache-Control", "no-store");

        webView.loadUrl(this.url_production,noCacheHeaders);
    }

    public boolean goBack(){
        if(webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return false;
     }

     public void onResume(){
        webView.onResume();
     }

}
