package com.tissini.webview;


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
import android.webkit.JsResult;
import android.webkit.ValueCallback;

import android.webkit.WebChromeClient;
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
import com.tissini.webview.models.MywebChromeClient;
import com.tissini.webview.models.MywebViewClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    WebSettings webSettings;
    String url = "https://tissini.app";
    private final static  String CHANEL_ID = "tissini";
    private final static  String CHANEL_ID_NAME = "tissini";
    private final static int NOTIFICATION_ID = 0;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    //nuevo
   // private InterestApi interestApi ;

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

        PushNotifications.start(getApplicationContext(), getString(R.string.instanceId));
        PushNotifications.addDeviceInterest("general");

//        PushNotifications.stop();
//        PushNotifications.removeDeviceInterest("general");
//        PushNotifications.clearDeviceInterests();

        /// nuevo
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.1.5:8000/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        interestApi = retrofit.create(InterestApi.class);
       // getInterests();

        // aqui termina lo nuevo

        swipeRefreshLayout = findViewById(R.id.swipe);
        progressBar        = (ProgressBar) findViewById(R.id.progressBar);
        webView            = (WebView) findViewById(R.id.webview);

        progressBar.setVisibility(View.INVISIBLE);
        getSupportActionBar().hide();

        webView.setWebChromeClient(new MywebChromeClient());
        webView.setWebViewClient(new MywebViewClient(this.progressBar,this.webView,this));
        webView.addJavascriptInterface(new WebAppInterface(this), "Webview");

        loadWebview();

        onRefresh(); // metodo para hacer refresh al webview


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
        webSettings.setUserAgentString(webSettings.getUserAgentString()+ " " + getString(R.string.user_agent_suffix));

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
        super.onPause();
    }

    @Override
    protected void onResume() {
        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(this, new PushNotificationReceivedListener() {
            @Override
            public void onMessageReceived(RemoteMessage remoteMessage) {
                createNotificationChanel();
                CreateNotification(remoteMessage);
            }

        });
        webView.onResume();
        super.onResume();
    }

    @Override
    public void onBackPressed(){
        if(webView.canGoBack()){
            webView.goBack();
        }else{
            super.onBackPressed();
        }
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

        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,num,intent,PendingIntent.FLAG_ONE_SHOT);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
    }


    // esto es nuevo
   /* private void createInterest(String user_id,String user_stage ) {
        Interest interest =  new Interest(user_id,user_stage);
        Call<Interest> call = interestApi.createInterest(interest);

        call.enqueue(new Callback<Interest>() {
            @Override
            public void onResponse(Call<Interest> call, Response<Interest> response) {
                if(!response.isSuccessful()){
                    System.err.println("XXXXXXXXXXXXXXXXXXXXXXX ERROR AL PROCESAR SOLICITUS DESDE RESPONSE");
                }

                Interest interest = response.body();

                System.out.println("user_id => "+interest.getUser_id());
                System.out.println("stage_id => "+interest.getUser_stage());
            }

            @Override
            public void onFailure(Call<Interest> call, Throwable t) {
                System.err.println("XXXXXXXXXXXXXXXXXXXXXXX ERROR AL PROCESAR SOLICITUS DESDE FAILURE => "+t.getMessage());
            }
        });
    }*/
    // esto es nuevo
   /* private void getInterests( ) {
        Call<List<Interest>> call = interestApi.getInterests();

        call.enqueue(new Callback<List<Interest>>() {
            @Override
            public void onResponse(Call<List<Interest>> call, Response<List<Interest>> response) {
                if(!response.isSuccessful()){
                    System.err.println("XXXXXXXXXXXXXXXXXXXXXXX ERROR AL PROCESAR SOLICITUS DESDE RESPONSE");
                }
                List<Interest> interests = response.body();

                for (Interest interest : interests ){
                    System.out.println("ID => "+interest.getId());
                    System.out.println("NAME => "+interest.getName());
                    System.out.println("STATUS => "+interest.getStatus());
                }
            }

            @Override
            public void onFailure(Call<List<Interest>> call, Throwable t) {
                System.err.println("XXXXXXXXXXXXXXXXXXXXXXX ERROR AL PROCESAR SOLICITUS DESDE FAILURE "+t.getMessage());
            }
        });

    }
*/

}