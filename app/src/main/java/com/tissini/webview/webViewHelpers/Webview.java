package com.tissini.webview.webViewHelpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tissini.webview.MainActivity;
import com.tissini.webview.R;

import java.util.HashMap;
import java.util.Map;

public class Webview {

    public WebView webView;
    public Activity activity;
    public WebSettings webSettings;
    public ProgressBar progressBar;
    public SwipeRefreshLayout swipeRefreshLayout;

    public Webview(Activity activity){
        this.activity = activity;
        this.webView = (WebView) activity.findViewById(R.id.webview);
        this.progressBar = (ProgressBar) activity.findViewById(R.id.progressBar);
        this.webSettings = this.webView.getSettings();
        this.swipeRefreshLayout = (SwipeRefreshLayout) activity.findViewById(R.id.swipe);
        onRefresh();
    }

    public void webSettings(){
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(false);
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE  );
        webSettings.setUserAgentString(webSettings.getUserAgentString()+ " " + this.activity.getString(R.string.user_agent_suffix));
    }

    public void loadUrl(Intent intent, String url){

        webView.setWebChromeClient(new MywebChromeClient(activity));
        webView.setWebViewClient(new MywebViewClient(this.progressBar,this.webView,this.activity,intent));
        webView.addJavascriptInterface(new WebAppInterface(activity), "Webview");
        webSettings();

        Uri appLinkData = intent.getData();
        if(appLinkData != null)
            url = appLinkData.toString();

        String link = intent.getStringExtra("link");
        if (link != null)
            url = link;

        Map<String, String> noCacheHeaders = new HashMap<String, String>(2);
        noCacheHeaders.put("Cache-Control", "no-cache");
        noCacheHeaders.put("Cache-Control", "no-store");

        webView.loadUrl(url,noCacheHeaders);
    }

    public  void onRefresh(){
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        webView.reload();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    public boolean goBack(){
        if(webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return false;
     }
     
}
