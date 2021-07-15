package com.tissini.webview.controllers;

import com.tissini.webview.services.VersionServices;

import android.webkit.WebView;

public class VersionController {

    WebView webview;
    VersionServices versionServices = new VersionServices();

    public VersionController(WebView webview){
        this.webview = webview;
    }

    public void getVersion(){
        versionServices.getVersion(webview);
    }

}
