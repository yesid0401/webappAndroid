package com.tissini.webview;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.pusher.pushnotifications.PushNotifications;
import com.tissini.webview.webViewHelpers.MywebChromeClient;
import com.tissini.webview.webViewHelpers.MywebViewClient;
import com.tissini.webview.webViewHelpers.WebAppInterface;
import com.tissini.webview.webViewHelpers.Webview;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
   // WebView webView;
    Webview webview;
    WebSettings webSettings;
    String url = "https://stage.tissini.dev/ebL0EI";
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!isOnline(this)){
            Toast toast = Toast.makeText(this, "Conectese a una red con internet", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        swipeRefreshLayout = findViewById(R.id.swipe);
//        progressBar        = (ProgressBar) findViewById(R.id.progressBar);
//        webView            = (WebView) findViewById(R.id.webview);
        PushNotifications.start(getApplicationContext(), getString(R.string.instanceId));
//        progressBar.setVisibility(View.INVISIBLE);
        getSupportActionBar().hide();

        //loadWebview();
       // onRefresh();

        webview = new Webview(this);
        webview.loadUrl(getIntent(),this.url);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
       // webview.onResume();
        super.onResume();
    }

    @Override
    public void onBackPressed(){

        if(!webview.goBack()){
            super.onBackPressed();
        }
//        if(webView.canGoBack()){
//            webView.goBack();
//        }else{
//            super.onBackPressed();
//        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;

            Uri result = intent == null || resultCode != MainActivity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }


    }

//    public  void onRefresh(){
//        swipeRefreshLayout.setOnRefreshListener(
//                new SwipeRefreshLayout.OnRefreshListener() {
//                    @Override
//                    public void onRefresh() {
//                        webView.reload();
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }
//        );
//    }

//    public void loadWebview(){
//        webView.setWebChromeClient(new MyWebChromClientClass());
//        webView.setWebViewClient(new MywebViewClient(this.progressBar,this.webView,this,getIntent()));
//        webView.addJavascriptInterface(new WebAppInterface(this), "Webview");
//        webSettings(webView);
//
//        Uri appLinkData = getIntent().getData();
//        if(appLinkData != null)
//            this.url = appLinkData.toString();
//
//        String link = getIntent().getStringExtra("link");
//        if (link != null)
//            this.url = link;
//
//        Map<String, String> noCacheHeaders = new HashMap<String, String>(2);
//        noCacheHeaders.put("Cache-Control", "no-cache");
//        noCacheHeaders.put("Cache-Control", "no-store");
//
//        webView.loadUrl(this.url,noCacheHeaders);
//    }

//    public void webSettings(WebView webView){
//        webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setDatabaseEnabled(false);
//        webSettings.setAppCacheEnabled(false);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE  );
//        webSettings.setUserAgentString(webSettings.getUserAgentString()+ " " + getString(R.string.user_agent_suffix));
//    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public  class MyWebChromClientClass extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
        // For 3.0+ Devices (Start)
        // onActivityResult attached before constructor
        protected void openFileChooser(ValueCallback uploadMsg, String acceptType)
        {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
        }


        // For Lollipop 5.0+ Devices
        public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
        {
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }

            uploadMessage = filePathCallback;

            Intent intent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                intent = fileChooserParams.createIntent();
            }
            try
            {
                startActivityForResult(intent, REQUEST_SELECT_FILE);
            } catch (ActivityNotFoundException e)
            {
                uploadMessage = null;
                return false;
            }
            return true;
        }

        //For Android 4.1 only
        protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
        {
            mUploadMessage = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
        }

        protected void openFileChooser(ValueCallback<Uri> uploadMsg)
        {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }
    }


}