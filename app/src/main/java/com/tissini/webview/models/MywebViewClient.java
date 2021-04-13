package com.tissini.webview.models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pusher.pushnotifications.PushNotifications;
import com.tissini.webview.WebAppInterface;
import com.tissini.webview.interfaces.InterestApi;
import com.tissini.webview.interfaces.JsonPlaceHolderI;
import com.tissini.webview.interfaces.NotificationI;

import java.util.List;

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
    private Context mContext;
    private NotificationI notificationI;
    private InterestApi interestApi ;
    private JsonPlaceHolderI jsonPlaceHolderI;
    public String status;

    public MywebViewClient(ProgressBar progressBar, WebView webView, Activity activity,Intent intent,Context mContext){
        this.progressBar = progressBar;
        this.webView = webView;
        this.activity = activity;
        this.intent = intent;
        this.mContext = mContext;
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.1.2:8000/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        notificationI = retrofit.create(NotificationI.class);
//        interestApi = retrofit.create(InterestApi.class);
    }

    public MywebViewClient(){
        Retrofit retrofit =  new Retrofit.Builder()
                .baseUrl("https://io.tissini.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

                ;
        //notificationI = retrofit.create(NotificationI.class);
        jsonPlaceHolderI = retrofit.create(JsonPlaceHolderI.class);
        //interestApi = retrofit.create(InterestApi.class);
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
        // esto lo tengo quequitar
       // WebAppInterface webAppInterface = new WebAppInterface(this.mContext,webView);
       // webAppInterface.getCart();

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

    public void readNotification(String idClient,String idNotification ) {
        Notification notification =  new Notification(idClient,idNotification);
        Call<Notification> call = notificationI.readNotification(notification);

        call.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if(!response.isSuccessful()){
                    System.err.println(" ERROR AL PROCESAR SOLICITUS DESDE RESPONSE");
                }

            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                System.err.println(" ERROR AL PROCESAR SOLICITUS DESDE FAILURE => "+t.getMessage());
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
                    System.err.println(" ERROR AL PROCESAR SOLICITUS DESDE RESPONSE");
                }
            }

            @Override
            public void onFailure(Call<Interest> call, Throwable t) {
                System.err.println(" ERROR AL PROCESAR SOLICITUS DESDE FAILURE => "+t.getMessage());
            }
        });
    }


    public String getJsonPlaceHolder(String token ) {

        try {
            Call<JsonPlaceHolder> call = jsonPlaceHolderI.getPost("Bearer " + token);

            call.enqueue(new Callback<JsonPlaceHolder>() {
                @Override
                public void onResponse(Call<JsonPlaceHolder> call, Response<JsonPlaceHolder> response) {

                    if (!response.isSuccessful()) {
                        System.err.println("ERROR AL PROCESAR SOLICITUS DESDE RESPONSE");
                    }

                    Gson gson = new Gson();
                    String data = gson.toJson(response.body().getShopping_cart());
                    System.out.println(data);
                    JsonParser parser = new JsonParser();
                    JsonElement jsonTree = parser.parse(data);
                    JsonObject jsonObject = jsonTree.getAsJsonObject();
                    JsonElement statusj   = jsonObject.get("status");

                    if(jsonObject.get("status") != null){
                        status = (String) statusj.getAsString();
                    }


                }

                @Override
                public void onFailure(Call<JsonPlaceHolder> call, Throwable t) {
                    System.err.println("ERROR AL PROCESAR SOLICITUS DESDE FAILURE " + t.getMessage());
                }
            });

        }
        catch (Exception e){
            System.out.println("Exception => " + e.getMessage());
        }

        System.out.println("IMPRIMIENDO DESDE MYWEBCLIENT");
        return status;
    }

    public void getInterests( ) {
        String auth = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoxLCJpZGVudGlmaWNhdGlvbiI6IjEwODM0MzQ4OTgiLCJmaXJzdE5hbWUiOiJ5ZXNpZCBlbWlybyAiLCJsYXN0TmFtZSI6InNhbmNoZXogcmFtb3MgIiwiZW1haWwiOiJ5ZXNpZHNAdGlzc2luaS5jb20gIiwidGVsZXBob25lIjoiMzAwNjMxNjE2MCIsInVzZXIiOiJ5ZXNpZDEwMTAiLCJwYXNzd29yZCI6IiQyYiQxMCRlNXcxZUJ3cXB0T01NUGJqdWVwZTgucnkxWk9pRHpPMzRHbHlacXF3NXZvNWFrRjJyRHE3LiIsInN0YXR1cyI6InRydWUiLCJyb2xJZCI6MiwiY3JlYXRlZEF0IjoiMjAyMS0wNC0wM1QyMjoyMzozOC4wMDBaIiwidXBkYXRlZEF0IjoiMjAyMS0wNC0wNlQyMzozMDoxMC4wMDBaIn0sImlhdCI6MTYxNzkwMDA5NywiZXhwIjoxNjE3OTg2NDk3fQ.SYEBs7R6KArYqAEYJda4XysaBsZEknp43ZDqXSvape8";
        Call<List<Interest>> call = interestApi.getInterests( "Bearer "+auth);
        call.enqueue(new Callback<List<Interest>>() {
        @Override
        public void onResponse(Call<List<Interest>> call, Response<List<Interest>> response) {
            if(!response.isSuccessful()){
                System.err.println(" ERROR AL PROCESAR SOLICITUS DESDE RESPONSE");
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
            System.err.println(" ERROR AL PROCESAR SOLICITUS DESDE FAILURE "+t.getMessage());
        }
    });
}


//// PROBANDO LA LOGICA ENN ESTE ARCHIVO///


}
