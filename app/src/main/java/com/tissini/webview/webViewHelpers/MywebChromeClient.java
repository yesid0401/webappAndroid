package com.tissini.webview.webViewHelpers;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.core.app.ActivityCompat;

public class MywebChromeClient extends WebChromeClient  {
    public ValueCallback<Uri> mUploadMessage;
    public static ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    public final static int FILECHOOSER_RESULTCODE = 1;
    Activity a;
    

    public MywebChromeClient (Activity a ){
        this.a = a;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }

    // For Lollipop 5.0+ Devices
    public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
    {
        if (uploadMessage != null) {
            uploadMessage.onReceiveValue(null);
            uploadMessage = null;
        }

        uploadMessage = filePathCallback;
        System.out.println("uploadMessage  DESEDE WEBCHROM=> XXXX "+uploadMessage);
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            intent = fileChooserParams.createIntent();
        }
        try
        {
            ActivityCompat.startActivityForResult(a,intent, REQUEST_SELECT_FILE,null);
        } catch (ActivityNotFoundException e)
        {
            uploadMessage = null;
            return false;
        }
        return true;
    }



}


