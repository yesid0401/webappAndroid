package com.tissini.webview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.webkit.ValueCallback;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.PushNotificationReceivedListener;
import com.pusher.pushnotifications.PushNotifications;

import java.util.Random;



public class MainActivity extends AppCompatActivity {
    WebView webView;
    WebSettings webSettings;
    String url = "https://tissini.app/";
    private final static  String CHANEL_ID = "tissini";
    private final static  String CHANEL_ID_NAME = "tissini";
    private final static int NOTIFICATION_ID = 0;
    private static ConnectivityManager manager;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

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

        swipeRefreshLayout = findViewById(R.id.swipe);
        progressBar        = (ProgressBar) findViewById(R.id.progressBar);
        webView            = (WebView) findViewById(R.id.webview);

        progressBar.setVisibility(View.INVISIBLE);
        getSupportActionBar().hide();

        webView.setWebViewClient(new MywebViewClient());

        loadWebview();

        onRefresh(); // metodo para hacer refresh al webview

        PushNotifications.start(getApplicationContext(), "c5b50bd9-f225-4b64-9352-b9851a952b1f");
        PushNotifications.addDeviceInterest("hello");

        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(this, new PushNotificationReceivedListener() {
            @Override
            public void onMessageReceived(RemoteMessage remoteMessage) {
                createNotificationChanel();
                CreateNotification(remoteMessage);
            }
        });

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

    public void loadWebview(){

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);

        webSettings.setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
        webSettings.setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        Uri appLinkData = getIntent().getData();
        if(appLinkData != null)
            this.url = appLinkData.toString();

        String link = getIntent().getStringExtra("link");
        if (link != null)
            this.url = link;

        webView.loadUrl(this.url);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        webView.onResume();
        super.onResume();
    }

    public void createNotificationChanel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Notificacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANEL_ID,CHANEL_ID_NAME,NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void CreateNotification(RemoteMessage remoteMessage){

        String body = remoteMessage.getNotification().getBody();
        String title = remoteMessage.getNotification().getTitle();
        String link = remoteMessage.getData().get("link");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),CHANEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setColor(Color.parseColor("#FF4081"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        Intent intent = new Intent(MainActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("link",link);

        Random rnd = new Random();
        int num = rnd.nextInt(25);

        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,num,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
    }


    @Override
    public void onBackPressed(){
        if(webView.canGoBack()){
            webView.goBack();
        }else{
            //super.onBackPressed();
            //System.exit(0);
            //  finish();

        }
    }

    private class MywebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView wv, String url) {
            if(url.startsWith("whatsapp:") || url.startsWith("tel:") || url.startsWith("intent://") || url.startsWith("http://")) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    webView.goBack();
                    return true;
                }catch (android.content.ActivityNotFoundException e){
                    System.out.println("Error with " + url + ": " + e.toString());
                }

            }
            return false;
        }

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

                PushNotifications.addDeviceInterest("Login");
                PushNotifications.addDeviceInterest(user_id);
                PushNotifications.addDeviceInterest(user_stage);
                PushNotifications.removeDeviceInterest("noLogin");

                if(!user_escalafon.equals("null"))
                    PushNotifications.addDeviceInterest(user_escalafon);

            }else{
                PushNotifications.clearDeviceInterests();
                PushNotifications.addDeviceInterest("noLogin");
                PushNotifications.addDeviceInterest("general");
            }
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
    };

}