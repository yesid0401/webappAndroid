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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pusher.pushnotifications.PushNotifications;
import com.tissini.webview.BuildConfig;
import com.tissini.webview.interfaces.VersionI;

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
    private VersionI versionI;
    public MywebViewClient(ProgressBar progressBar, WebView webView, Activity activity,Intent intent){
        this.progressBar = progressBar;
        this.webView = webView;
        this.activity = activity;
        this.intent = intent;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://io.tissini.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        versionI = retrofit.create(VersionI.class);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView wv, String url) {
        if(url.startsWith("whatsapp:") || url.startsWith("tel:") || url.startsWith("intent://") || url.startsWith("http://") || url.startsWith("https://io.tissini")) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                activity.startActivity(intent);
                if(!url.startsWith("https://io.tissini"))
                   webView.goBack();
                return true;
            }catch (android.content.ActivityNotFoundException e){
                System.out.println("Error with " + url + ": " + e.toString());
            }

        }
        return false;
    }

    @Override
    public void onPageFinished (WebView view,String url){

        progressBar.setVisibility(View.INVISIBLE);
        getVersion();
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

    /// Methods

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

            PushNotifications.addDeviceInterest("general");
            PushNotifications.addDeviceInterest(user_id);
            PushNotifications.addDeviceInterest("Login");
            PushNotifications.addDeviceInterest(user_stage);
            PushNotifications.removeDeviceInterest("noLogin");

            System.out.println(PushNotifications.getDeviceInterests());

            if(!user_escalafon.equals("null"))
                PushNotifications.addDeviceInterest(user_escalafon);
        }else{
            PushNotifications.clearDeviceInterests();
            PushNotifications.addDeviceInterest("noLogin");
            System.out.println(PushNotifications.getDeviceInterests());
        }
    }

    public void getVersion(){
        try{
            Call<Version> call = versionI.getVersion();
            call.enqueue(new Callback<Version>() {
                @Override
                public void onResponse(Call<Version> call, Response<Version> response) {

                    if(!response.isSuccessful()){
                        System.out.println("Error al procesar la solicitud");
                        return;
                    }

                    Gson gson = new Gson();
                    String data = gson.toJson(response.body().getVersion_code());
                    System.out.println("VERSION ACTUAL => "+data);

                    int version_code_play_store = Integer.parseInt(data);

                    int version_actual = BuildConfig.VERSION_CODE;

                    if(version_actual < version_code_play_store){
                        webView.loadUrl("javascript:updateAvailable('true')");
                    }
                }

                @Override
                public void onFailure(Call<Version> call, Throwable t) {
                    System.out.println("Error al procesar la solicitud "+ t.getMessage());
                }
            });
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
