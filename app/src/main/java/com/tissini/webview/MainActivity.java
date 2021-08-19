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
import com.tissini.webview.webViewHelpers.WebChromeClients;
import com.tissini.webview.webViewHelpers.Webview;
import static com.tissini.webview.helpers.Functions.goToThePlayStore;
import static com.tissini.webview.helpers.Functions.isOnline;
import static com.tissini.webview.helpers.Functions.openApplication;

public class MainActivity extends AppCompatActivity {
    Webview webview;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PushNotifications.start(getApplicationContext(), getString(R.string.instanceId));

        if(!isOnline(this)){
            Toast toast = Toast.makeText(this, "Conectese a una red con internet", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }

        String updateAppInPlayStore = getIntent().getStringExtra("updateAppInPlayStore");
        if(updateAppInPlayStore != null) {
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(0);
                goToThePlayStore(this);
        }

        String link = getIntent().getStringExtra("link");
        if(link != null) {
            if (link.startsWith("https://www.youtube.com/")) {
                openApplication(link,"com.google.android.youtube",this);
            }
            if (link.startsWith("https://us02web.zoom.us/")) {
                openApplication(link,"us.zoom.videomeetings",this);
            }
            if (link.startsWith("https://tissini.com/")) {
               openApplication(link,"com.android.chrome",this);
            }

            if(link.startsWith("https://www.facebook.com/") || link.startsWith("https://play.google.com/store/apps/details?id=com.tissini.webview")){
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(link));
                startActivity(i);
            }
        }

        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        webview = new Webview(this,getIntent());
        webview.loadUrl();
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