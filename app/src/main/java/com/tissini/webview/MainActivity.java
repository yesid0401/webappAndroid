package com.tissini.webview;

import androidx.appcompat.app.AppCompatActivity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.widget.Toast;
import com.pusher.pushnotifications.PushNotifications;
import com.tissini.webview.helpers.Functions;
import com.tissini.webview.webViewHelpers.WebChromeClients;
import com.tissini.webview.webViewHelpers.Webview;

public class MainActivity extends AppCompatActivity {
    Webview webview;
    String url_stage = "https://stage.tissini.dev/";
    String url_production = "https://tissini.app/";
    String url = url_production;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String updateAppInPlayStore = getIntent().getStringExtra("updateAppInPlayStore");
        if(updateAppInPlayStore != null) {
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(0);
                Functions.goToThePlayStore(this);
        }

        if(!Functions.isOnline(this)){
            Toast toast = Toast.makeText(this, "Conectese a una red con internet", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }

        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PushNotifications.start(getApplicationContext(), getString(R.string.instanceId));
        getSupportActionBar().hide();
        webview = new Webview(this,getIntent());
        webview.loadUrl(this.url);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        webview.onResume();
        super.onResume();
    }

    @Override
    public void onBackPressed(){
        if(!webview.goBack()){
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if ( WebChromeClients.uploadMessage == null)
                    return;
                WebChromeClients.uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                WebChromeClients.uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;

            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }
}