package com.tissini.webview.models;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pusher.pushnotifications.PushNotifications;
import com.tissini.webview.interfaces.InterestApi;
import com.tissini.webview.interfaces.NotificationI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MywebViewClient extends WebViewClient {

    Activity activity;
    WebView webView;
    ProgressBar progressBar;
    Intent intent;
    private NotificationI notificationI;
    private InterestApi interestApi ;

    public MywebViewClient(ProgressBar progressBar, WebView webView, Activity activity,Intent intent){
        this.progressBar = progressBar;
        this.webView = webView;
        this.activity = activity;
        this.intent = intent;
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.1.2:8000/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        notificationI = retrofit.create(NotificationI.class);
//        interestApi = retrofit.create(InterestApi.class);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView wv, String url) {
        if(url.startsWith("whatsapp:") || url.startsWith("tel:") || url.startsWith("intent://") || url.startsWith("http://")) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                activity.startActivity(intent);
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

            PushNotifications.addDeviceInterest(user_id);
            PushNotifications.addDeviceInterest("Login");
            PushNotifications.addDeviceInterest(user_stage);
            PushNotifications.removeDeviceInterest("noLogin");

            // esto es nuevo
            String client_id = user_id;
            String client_stage = user_stage;
          //  createInterest(client_id,client_stage);


            String idNotification = intent.getStringExtra("idNotification");
            if (idNotification != null){
                String idClient = user_id;
                //readNotification(idClient,idNotification);
            }
            //aqui termina lo nuevo

            System.out.println(PushNotifications.getDeviceInterests());

            if(!user_escalafon.equals("null"))
                PushNotifications.addDeviceInterest(user_escalafon);



        }else{
            PushNotifications.clearDeviceInterests();
            PushNotifications.addDeviceInterest("noLogin");
            // PushNotifications.addDeviceInterest("general");
            System.out.println(PushNotifications.getDeviceInterests());
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

    private void readNotification(String idClient,String idNotification ) {
        Notification notification =  new Notification(idClient,idNotification);
        Call<Notification> call = notificationI.readNotification(notification);

        call.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if(!response.isSuccessful()){
                    System.err.println("XXXXXXXXXXXXXXXXXXXXXXX ERROR AL PROCESAR SOLICITUS DESDE RESPONSE");
                }

                Notification notification = response.body();

                System.out.println("idClient => "+notification.getIdClient());
                System.out.println("idNotification => "+notification.getIdNotification());
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                System.err.println("XXXXXXXXXXXXXXXXXXXXXXX ERROR AL PROCESAR SOLICITUS DESDE FAILURE => "+t.getMessage());
            }
        });
    }

    // esto es nuevo
    private void createInterest(String client_id,String client_stage ) {
        Interest interest =  new Interest(client_id,client_stage);
        Call<Interest> call = interestApi.createInterest(interest);

        call.enqueue(new Callback<Interest>() {
            @Override
            public void onResponse(Call<Interest> call, Response<Interest> response) {
                if(!response.isSuccessful()){
                    System.err.println("XXXXXXXXXXXXXXXXXXXXXXX ERROR AL PROCESAR SOLICITUS DESDE RESPONSE");
                }
            }

            @Override
            public void onFailure(Call<Interest> call, Throwable t) {
                System.err.println("XXXXXXXXXXXXXXXXXXXXXXX ERROR AL PROCESAR SOLICITUS DESDE FAILURE => "+t.getMessage());
            }
        });
    }
}
